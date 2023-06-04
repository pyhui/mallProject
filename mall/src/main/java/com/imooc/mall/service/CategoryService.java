package com.imooc.mall.service;

import com.imooc.mall.response.CommonReturnType;

import java.util.Set;

/**
 * Created by Peng on 2023/6/1 21:04
 */
public interface CategoryService {
    //查询所有
    CommonReturnType selectAll();

    //查询子类id
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
