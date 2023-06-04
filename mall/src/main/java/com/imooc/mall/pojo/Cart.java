package com.imooc.mall.pojo;

import lombok.Data;

/**
 * Created by Peng on 2023/6/3 17:20
 */
@Data
public class Cart {
    //商品id
    private Integer productId;
    //购物车内商品数量
    private Integer quantity;
    //购物车内商品是否选中
    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
