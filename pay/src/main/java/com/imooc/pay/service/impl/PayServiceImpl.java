package com.imooc.pay.service.impl;

import com.imooc.pay.dao.PayInfoMapper;
import com.imooc.pay.enums.PayPlatformEnum;
import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Peng on 2023/5/21 17:22
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    /**
     * 创建/发起支付
     * @param orderId
     * @param amount
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);
        //发起支付
        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(bestPayTypeEnum);
        payRequest.setOrderId(orderId);
        payRequest.setOrderName("4714734-最好的支付sdk");
        payRequest.setOrderAmount(amount.doubleValue());

        PayResponse response = bestPayService.pay(payRequest);
        log.info("发起支付 response={}",response);

        return response;
    }

    /**
     * 异步通知处理
     * @param notifyData
     */
    @Override
    public String asyncNotify(String notifyData) {
        //1.签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 response={}", payResponse);

        //2.金额校验（从数据库查订单）
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));

        if (payInfo == null) {
            //比较严重（正常情况下不会发生）发出警告：钉钉、短信
            throw new RuntimeException("通过orderNo查询到的结果是null");
        }
        //支付状态为未完成
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的支付金额和数据库中的不一致，orderNo=" + payResponse.getOrderId());
            }
            //3.修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());  //支付平台生成的交易单号
            payInfo.setUpdateTime(null);
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        //TODO pay发送MQ消息，mall接收MQ消息

        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            //4.告诉微信不要再通知了
            return "<xml>\n" +
                    "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "<return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            //4.告诉支付宝不要再通知了
            return "success";
        }
        throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
        return payInfo;
    }
}
