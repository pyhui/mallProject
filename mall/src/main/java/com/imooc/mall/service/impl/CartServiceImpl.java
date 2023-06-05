package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.controller.vo.CartProductVO;
import com.imooc.mall.controller.vo.CartVO;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.imooc.mall.enums.ProductStatusEnum.DELETE;
import static com.imooc.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * Created by Peng on 2023/6/3 15:43
 */
@Service
public class CartServiceImpl implements CartService {
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    private Gson gson = new Gson();  //提供java对象序列化和反序列化功能

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public CommonReturnType add(Integer uid, CartAddForm form) {
        Integer quantity = 1;  //默认添加购物车数量+1
        Product product = productMapper.selectByPrimaryKey(form.getProductId());
        //判断商品是否存在
        if (product == null) {
            return CommonReturnType.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //商品在售状态
        if (product.getStatus().equals(OFF_SALE.getCode()) ||
                product.getStatus().equals(DELETE.getCode())) {
            return CommonReturnType.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品库存是否充足
        if (product.getStock() <= 0) {
            return CommonReturnType.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //写入redis , key:cart_1
        //redis内只需存储商品id,数量，是否被选中；价格等等信息可能会变，不应存入redis
        Cart cart;
        //这里使用hashmap结构存储数据
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;

        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            //redis内没有这个商品，需要新增
            cart = new Cart(form.getProductId(), quantity, form.getSelected());
        } else {
            //已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);  //将json字符串反序列化为cart对象
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));

        return list(uid);
    }

    //优化list方法，把SQL语句从for循环中提出来
    @Override
    public CommonReturnType list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;
        Map<String, String> entries = opsForHash.entries(redisKey);

        Set<Integer> productIdSet = new HashSet<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            productIdSet.add(productId);
        }
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.getId(), product);
        }

        boolean selectAll = true;  //购物车商品是否全选
        Integer totalQuantity = 0;    //购物车商品数量
        BigDecimal totalPrice = new BigDecimal(0);  //购物车商品总价
        CartVO cartVO = new CartVO();
        List<CartProductVO> cartProductVOList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            Product product = productMap.get(productId);
            if (product != null) {
                CartProductVO cartProductVO = new CartProductVO(
                        product.getId(),
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                if (!cart.getProductSelected()) {
                    selectAll = false;
                }
                //计算购物车总价，只计算选中的
                if (cart.getProductSelected()) {
                    totalPrice = totalPrice.add(cartProductVO.getProductTotalPrice());
                }

                cartProductVOList.add(cartProductVO);
            }
            //购物车中商品总数量无需写入if语句中
            totalQuantity += cart.getQuantity();
        }

        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setSelectAll(selectAll);
        cartVO.setCartTotalQuantity(totalQuantity);
        cartVO.setCartTotalPrice(totalPrice);
        return CommonReturnType.success(cartVO);

    }

/*
    @Override
    public CommonReturnType list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;
        Map<String, String> entries = opsForHash.entries(redisKey);

        boolean selectAll = true;  //购物车商品是否全选
        Integer totalQuantity = 0;    //购物车商品数量
        BigDecimal totalPrice = new BigDecimal(0);  //购物车商品总价
        CartVO cartVO = new CartVO();
        List<CartProductVO> cartProductVOList = new ArrayList<>();
        //
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            //TODO 需要优化，使用MySQL里面的in
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVO cartProductVO = new CartProductVO(
                        product.getId(),
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                if (!cart.getProductSelected()) {
                    selectAll = false;
                }
                //计算购物车总价，只计算选中的
                if (cart.getProductSelected()) {
                    totalPrice = totalPrice.add(cartProductVO.getProductTotalPrice());
                }

                cartProductVOList.add(cartProductVO);
            }
            //购物车中商品总数量无需写入if语句中
            totalQuantity += cart.getQuantity();
        }
        //
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setSelectAll(selectAll);
        cartVO.setCartTotalQuantity(totalQuantity);
        cartVO.setCartTotalPrice(totalPrice);
        return CommonReturnType.success(cartVO);
    }
*/

    @Override
    public CommonReturnType update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //redis内没有这个商品，报错
            return CommonReturnType.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }

        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }
        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public CommonReturnType delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //redis内没有这个商品，报错
            return CommonReturnType.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        opsForHash.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }

    @Override
    public CommonReturnType selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;

        List<Cart> cartList = listForCart(uid);
        for (Cart cart : cartList) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public CommonReturnType unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;

        List<Cart> cartList = listForCart(uid);
        for (Cart cart : cartList) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public CommonReturnType sum(Integer uid) {
        Integer sum = 0;
        for (Cart cart : listForCart(uid)) {
            sum += cart.getQuantity();
        }
        return CommonReturnType.success(sum);
    }

    //用于查询出购物车中全部商品的列表
    private List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = "cart_" + uid;
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            cartList.add(cart);
        }
        return cartList;
    }
}
