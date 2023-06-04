package com.imooc.mall.service;

import com.imooc.mall.response.CommonReturnType;

/**
 * Created by Peng on 2023/6/2 15:50
 */
public interface ProductService {
    CommonReturnType list(Integer categoryId, Integer pageNum, Integer pageSize);

    CommonReturnType detail(Integer productId);
}
