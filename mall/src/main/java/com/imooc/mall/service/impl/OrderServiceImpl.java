package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.controller.vo.OrderItemVO;
import com.imooc.mall.controller.vo.OrderVO;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.OrderStatusEnum;
import com.imooc.mall.enums.PaymentTypeEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.ProductStatusEnum.DELETE;
import static com.imooc.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * Created by Peng on 2023/6/6 23:37
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private CartServiceImpl cartServiceImpl;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public CommonReturnType create(Integer uid, Integer shippingId) {
        //1.收货地址校验（总之要查询出来的），通过uid和shippingId两个参数进行查询
        Shipping shipping = shippingMapper.selectByIdAndUid(uid, shippingId);
        if (shipping == null) {
            return CommonReturnType.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }
        //2.获取购物车，校验（是否有商品、库存）
        //获取选中的商品
        List<Cart> cartList = cartServiceImpl.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)) {
            return CommonReturnType.error(ResponseEnum.CART_SELECT_IS_EMPTY);
        }
        //获取cartList里的productIdSet
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.getId(), product);
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        //遍历做校验
        for (Cart cart : cartList) {
            Integer productId = cart.getProductId();
            Product product = productMap.get(productId);
            if (product == null) {
                return CommonReturnType.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在，productId=" + cart.getProductId());
            }
            if (product.getStatus().equals(OFF_SALE.getCode())
                    || product.getStatus().equals(DELETE.getCode())) {
                return CommonReturnType.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                        product.getName() + "不是在售状态");
            }
            if (cart.getQuantity() > product.getStock()) {
                return CommonReturnType.error(ResponseEnum.PRODUCT_STOCK_ERROR,
                         product.getName() + "库存不足");
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //5.减库存
            product.setStock(product.getStock() - cart.getQuantity());
            //这个还是在for循环里面写sql
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row == 0) {
                return CommonReturnType.error(ResponseEnum.ERROR);
            }
        }
        //3.计算总价，只计算被选中的商品
        //4.生成订单，入库：order和order_item,添加事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder == 0) {
            return CommonReturnType.error(ResponseEnum.ERROR);
        }
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem == 0) {
            return CommonReturnType.error(ResponseEnum.ERROR);
        }
        //6.更新购物车（选中的商品）
        //redis有事务（打包命令），不能回滚
        for (Cart cart : cartList) {
            Integer productId = cart.getProductId();
            cartServiceImpl.delete(uid, productId);
        }
        //7.构造orderVO
        OrderVO orderVO = buildOrderVO(order, orderItemList, shipping);
        return CommonReturnType.success(orderVO);
    }

    @Override
    public CommonReturnType list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = buildOrderVO(order,
                    orderItemMap.get(order.getOrderNo()),   //数据对应不上会报空指针异常，可以在这一行加上告警
                    shippingMap.get(order.getShippingId()));
            orderVOList.add(orderVO);
        }

        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);

        return CommonReturnType.success(pageInfo);
    }

    @Override
    public CommonReturnType detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return CommonReturnType.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVO orderVO = buildOrderVO(order, orderItemList, shipping);
        return CommonReturnType.success(orderVO);
    }

    @Override
    public CommonReturnType cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return CommonReturnType.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //这里设定只有未付款的情况下才能取消
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            return CommonReturnType.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKey(order);
        if (row <= 0) {
            return CommonReturnType.error(ResponseEnum.ERROR);
        }
        return CommonReturnType.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单号：" + orderNo);
        }
        //只有未付款的情况下才能改变状态
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单号：" + orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date()); //最好实在payInfo里面拿
        int row = orderMapper.updateByPrimaryKey(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为'已支付'状态失败，订单号：" + orderNo);
        }
    }

    //order和orderItemList是对应的
    private OrderVO buildOrderVO(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);

        List<OrderItemVO> OrderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            OrderItemVOList.add(orderItemVO);
        }

        orderVO.setOrderItemVOList(OrderItemVOList);

        if (shipping != null) {
            orderVO.setShippingId(shipping.getId());
            orderVO.setShippingVO(shipping);
        }
        return orderVO;
    }

    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }

    //生成订单号，简单处理
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
