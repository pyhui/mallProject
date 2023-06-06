package com.imooc.mall.service;

import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.response.CommonReturnType;

/**
 * 收货地址
 * Created by Peng on 2023/6/6 16:05
 */
public interface ShippingService {
    //添加地址
    CommonReturnType add(Integer uid, ShippingForm form);
    //删除地址
    CommonReturnType delete(Integer uid, Integer shippingId);
    //修改地址
    CommonReturnType update(Integer uid, Integer shippingId, ShippingForm form);
    //查看地址列表
    CommonReturnType list(Integer uid, Integer pageNum, Integer pageSize);
}
