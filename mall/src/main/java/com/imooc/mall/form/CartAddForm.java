package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加商品
 * Created by Peng on 2023/6/3 11:50
 */
@Data
public class CartAddForm {
    @NotNull
    private Integer productId;
    private Boolean selected = true;
}
