package com.imooc.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Peng on 2023/5/29 16:59
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayAccountConfig {

    private String appId;

    private String privateKey;

    private String aliPayPublicKey;

    private String notifyUrl;

    private String returnUrl;
}
