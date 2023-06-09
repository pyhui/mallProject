package com.imooc.mall.dao;

import com.imooc.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);
    //通过uid和shippingId删除地址
    int deleteByIdAndUid(@Param("uid") Integer uid,
                         @Param("shippingId") Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);
    //通过uid获取地址
    List<Shipping> selectByUserId(Integer uid);
    //通过uid和shippingId两个参数进行查询
    Shipping selectByIdAndUid(@Param("uid") Integer uid,
                              @Param("shippingId") Integer shippingId);

    List<Shipping> selectByIdSet(@Param("shippingIdSet") Set<Integer> shippingIdSet);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}