package com.imooc.mall.dao;

import com.imooc.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    //对商品进行查询，查询它以及它下面的子类目商品
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    //查询购物车中的商品，用productIdSet中提供的productId进行查找
    List<Product > selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}