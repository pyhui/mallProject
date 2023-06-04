package com.imooc.mall.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 * Created by Peng on 2023/6/3 11:33
 */
@Data
public class CartVO {
    private List<CartProductVO> cartProductVOList;
    //购物车内商品是否全选
    private Boolean selectAll;
    //购物车内商品总价
    private BigDecimal cartTotalPrice;
    //购物车内商品总数量
    private Integer cartTotalQuantity;
}
