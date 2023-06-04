package com.imooc.pay.enums;

import com.imooc.pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * Created by Peng on 2023/5/24 16:45
 */
@Getter
public enum PayPlatformEnum {
    ALIPAY(1),
    WX(2),
    ;

    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("错误的支付平台：" + bestPayTypeEnum.name());
    }
}
