package com.imooc.mall.controller;

import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Peng on 2023/6/2 22:37
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public CommonReturnType list(@RequestParam(required = false) Integer categoryId,
                                 @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return productService.list(categoryId, pageNum, pageSize);
    }

    @GetMapping("/{productId}")
    public CommonReturnType detail(@PathVariable Integer productId) {
        return productService.detail(productId);
    }
}
