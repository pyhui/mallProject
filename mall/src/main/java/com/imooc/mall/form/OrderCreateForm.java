package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Peng on 2023/6/9 17:14
 */
@Data
public class OrderCreateForm {
    @NotNull
    private Integer shippingId;
}
