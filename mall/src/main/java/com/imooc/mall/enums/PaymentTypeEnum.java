package com.imooc.mall.enums;

import lombok.Getter;

/**
 * Created by Peng on 2023/6/7 17:09
 */
@Getter
public enum PaymentTypeEnum {
    PAY_ONLINE(1),
    ;

    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
