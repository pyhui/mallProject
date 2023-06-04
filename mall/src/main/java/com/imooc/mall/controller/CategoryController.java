package com.imooc.mall.controller;

import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Peng on 2023/6/1 21:52
 */
@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public CommonReturnType selectAll() {
        return categoryService.selectAll();
    }
}
