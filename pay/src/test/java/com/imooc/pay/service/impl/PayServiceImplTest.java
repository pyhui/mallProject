package com.imooc.pay.service.impl;

import com.imooc.pay.PayApplicationTests;
import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by Peng on 2023/5/21 17:56
 */
public class PayServiceImplTest extends PayApplicationTests {

    @Autowired
    private IPayService iPayService;

    @Test
    public void create() {
        iPayService.create("123456", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
}