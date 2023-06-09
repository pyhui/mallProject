package com.imooc.mall.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Peng on 2023/6/6 23:31
 */
@Data
public class OrderItemVO {
    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;
}
