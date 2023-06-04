package com.imooc.mall.controller.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Peng on 2023/6/2 15:32
 */
@Data
public class ProductVO {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
