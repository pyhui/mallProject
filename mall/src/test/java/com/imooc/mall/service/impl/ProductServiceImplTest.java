package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Peng on 2023/6/2 16:20
 */
public class ProductServiceImplTest extends MallApplicationTests {
    @Autowired
    private ProductService productService;

    @Test
    public void list() {
        CommonReturnType commonReturnType = productService.list(null, 2, 2);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void detail() {
        CommonReturnType commonReturnType = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }
}