package com.imooc.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Peng on 2023/5/23 17:00
 */
@Component
public class BestPayConfig {

    @Autowired
    private WxAccountConfig wxAccountConfig;

    @Autowired
    private AlipayAccountConfig alipayAccountConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig) {
        //支付宝支付配置
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(alipayAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(alipayAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipayAccountConfig.getAliPayPublicKey());
        aliPayConfig.setReturnUrl(alipayAccountConfig.getReturnUrl());   //支付后跳转地址
        aliPayConfig.setNotifyUrl(alipayAccountConfig.getNotifyUrl());

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        //配置
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());     //公众号Id
        wxPayConfig.setMchId(wxAccountConfig.getMchId());    //商户Id
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());  //商户密钥
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        //需外网可访问url，不能携带参数，示例如下
        //wxPayConfig.setNotifyUrl("http://imooc-mall.natapp1.cc/pay/notify");
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        return wxPayConfig;
    }
}
