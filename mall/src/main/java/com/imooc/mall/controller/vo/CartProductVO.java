package com.imooc.mall.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车内商品
 * Created by Peng on 2023/6/3 11:40
 */
@Data
public class CartProductVO {
    //商品id
    private Integer productId;
    //购物车内商品数量
    private Integer quantity;
    //商品名称
    private String productName;
    //商品描述
    private String productSubtitle;
    //商品主图
    private String productMainImage;
    //商品价格
    private BigDecimal productPrice;
    //商品状态
    private Integer productStatus;
    //商品总价格
    private BigDecimal productTotalPrice;
    //商品库存
    private Integer productStock;
    //商品是否选中
    private Boolean productSelected;

    public CartProductVO(Integer productId, Integer quantity, String productName, String productSubtitle, String productMainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.productMainImage = productMainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }
}
