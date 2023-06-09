package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Peng on 2023/6/7 15:29
 */
@Slf4j
//@Transactional
public class OrderServiceImplTest extends MallApplicationTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    private Integer uid = 1;
    private Integer shippingId = 4;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void before() {
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        CommonReturnType commonReturnType = cartService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void create() {
        CommonReturnType commonReturnType = orderService.create(uid, shippingId);
        log.info("create = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void list() {
        CommonReturnType commonReturnType = orderService.list(uid, 2, 2);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void detail() {
        CommonReturnType commonReturnType = orderService.detail(uid, Long.valueOf("1686143864091"));
        log.info("detail = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void cancel() {
        CommonReturnType commonReturnType = orderService.cancel(uid, Long.valueOf("1686215931728"));
        log.info("cancel = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }
}