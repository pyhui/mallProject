package com.imooc.mall.enums;

import lombok.Getter;

/**
 * Created by Peng on 2023/5/30 18:00
 */
@Getter
public enum ResponseEnum {
    ERROR(-1, "服务端错误"),
    SUCCESS(0, "成功"),
    PASSWORD_ERROR(1, "密码错误"),
    USERNAME_EXIST(2, "用户名已存在"),
    PARAM_ERROR(3, "参数错误"),
    EMAIL_EXIST(4, "邮箱已注册"),
    NEED_LOGIN(10, "用户未登录，请先登录"),
    USERNAME_OR_PASSWORD_ERROR(11,"用户名或密码错误"),
    PRODUCT_OFF_SALE_OR_DELETE(12, "商品下架或删除"),
    PRODUCT_NOT_EXIST(13, "商品不存在"),
    PRODUCT_STOCK_ERROR(14, "商品库存不足"),
    CART_PRODUCT_NOT_EXIST(15, "购物车内无此商品"),
    ;

    Integer code;
    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
