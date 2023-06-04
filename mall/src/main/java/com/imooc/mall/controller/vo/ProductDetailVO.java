package com.imooc.mall.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Peng on 2023/6/2 22:54
 */
@Data
public class ProductDetailVO {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
