package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Peng on 2023/6/6 16:25
 */
@Slf4j
public class ShippingServiceImplTest extends MallApplicationTests {

    @Autowired
    private ShippingService shippingService;

    @Test
    public void add() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("xyz");
        form.setReceiverPhone("010");
        form.setReceiverMobile("12345678888");
        form.setReceiverProvince("广东");
        form.setReceiverCity("深圳");
        form.setReceiverDistrict("南山区");
        form.setReceiverAddress("科兴科学园");
        form.setReceiverZip("000000");
        CommonReturnType commonReturnType = shippingService.add(13, form);
        log.info("add = {}", commonReturnType);
    }

    @Test
    public void delete() {
        CommonReturnType commonReturnType = shippingService.delete(13, 4);
        log.info("delete = {}", commonReturnType);
    }

    @Test
    public void update() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("xyz");
        CommonReturnType commonReturnType = shippingService.update(13, 5, form);
        log.info("update = {}", commonReturnType);
    }

    @Test
    public void list() {
        CommonReturnType commonReturnType = shippingService.list(13, 1, 10);
        log.info("list = {}", commonReturnType);
    }
}