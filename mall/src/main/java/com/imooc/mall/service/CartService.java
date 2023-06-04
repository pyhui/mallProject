package com.imooc.mall.service;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.response.CommonReturnType;

/**
 * Created by Peng on 2023/6/3 15:42
 */
public interface CartService {
    //添加购物车
    CommonReturnType add(Integer uid, CartAddForm form);

    CommonReturnType test(Integer uid);
    //购物车列表
    CommonReturnType list(Integer uid);

    //更新购物车
    CommonReturnType update(Integer uid, Integer productId, CartUpdateForm form);

    //删除购物车中商品
    CommonReturnType delete(Integer uid, Integer productId);

    //购物车中商品全选中
    CommonReturnType selectAll(Integer uid);

    //购物车中商品全都不要选中
    CommonReturnType unSelectAll(Integer uid);

    //获取购物车中所有商品数量总和
    CommonReturnType sum(Integer uid);
}
