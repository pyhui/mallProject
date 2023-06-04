package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peng on 2023/6/1 23:09
 */
@Slf4j
public class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void selectAll() {
        CommonReturnType commonReturnType = categoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), commonReturnType.getStatus());
    }

    @Test
    public void findSubCategoryId() {
        Set<Integer> resultSet = new HashSet<>();
        categoryService.findSubCategoryId(100001, resultSet);
        log.info("resultSet = {}", resultSet);
    }
}