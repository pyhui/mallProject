package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Peng on 2023/6/3 17:32
 */
@Slf4j
public class CartServiceImplTest extends MallApplicationTests {

    @Autowired
    private CartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //@Before
    @Test
    public void add() {
        log.info("【添加购物车】...");
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        CommonReturnType commonReturnType = cartService.add(1, form);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void list() {
        CommonReturnType commonReturnType = cartService.list(1);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void update() {
        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(5);
        cartUpdateForm.setSelected(false);
        CommonReturnType commonReturnType = cartService.update(1, 26, cartUpdateForm);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    //@After
    @Test
    public void delete() {
        log.info("【删除购物车】...");
        CommonReturnType commonReturnType = cartService.delete(1, 26);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void selectAll() {
        CommonReturnType commonReturnType = cartService.selectAll(1);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void unSelectAll() {
        CommonReturnType commonReturnType = cartService.unSelectAll(1);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void sum() {
        CommonReturnType commonReturnType = cartService.sum(1);
        log.info("list = {}", gson.toJson(commonReturnType));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }
}