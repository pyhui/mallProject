package com.imooc.mall.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Peng on 2023/6/1 20:59
 */
@Data
public class CategoryVO {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;
    //子类别
    private List<CategoryVO> subCategories;
}
