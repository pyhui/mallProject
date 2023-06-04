package com.imooc.mall.form;

import lombok.Data;

/**
 * Created by Peng on 2023/6/4 16:38
 */
@Data
public class CartUpdateForm {
    private Integer quantity;
    private Boolean selected = true;
}
