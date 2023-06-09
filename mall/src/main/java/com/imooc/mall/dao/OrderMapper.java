package com.imooc.mall.dao;

import com.imooc.mall.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectByUid(Integer uid);

    Order selectByOrderNo(Long orderNo);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}