package com.imooc.mall.enums;

import lombok.Getter;

/**
 * Created by Peng on 2023/6/2 23:13
 */
@Getter
public enum ProductStatusEnum {
    //1-在售 2-下架 3-删除
    ON_SALE(1),
    OFF_SALE(2),
    DELETE(3),
    ;

    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
