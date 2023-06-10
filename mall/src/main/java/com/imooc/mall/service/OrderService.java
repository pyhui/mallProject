package com.imooc.mall.service;

import com.imooc.mall.response.CommonReturnType;

/**
 * Created by Peng on 2023/6/6 23:35
 */
public interface OrderService {
    //创建订单
    CommonReturnType create(Integer uid, Integer shippingId);

    //订单列表
    CommonReturnType list(Integer uid, Integer pageNum, Integer pageSize);

    //查看订单详情
    CommonReturnType detail(Integer uid, Long orderNo);

    //取消订单
    CommonReturnType cancel(Integer uid, Long orderNo);

    //修改订单状态，未付款 => 已付款
    void paid(Long orderNo);
}
