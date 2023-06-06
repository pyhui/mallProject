package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.ShippingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng on 2023/6/6 16:06
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public CommonReturnType add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        shipping.setUserId(uid);
        BeanUtils.copyProperties(form, shipping);
        int row = shippingMapper.insertSelective(shipping);
        if (row == 0) {
            return CommonReturnType.error(ResponseEnum.ERROR);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());
        return CommonReturnType.success(map);
    }

    @Override
    public CommonReturnType delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row == 0) {
            return CommonReturnType.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }
        return CommonReturnType.successByMsg("删除地址成功");
    }

    @Override
    public CommonReturnType update(Integer uid, Integer shippingId, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0) {
            return CommonReturnType.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }
        return CommonReturnType.successByMsg("更新地址成功");
    }

    @Override
    public CommonReturnType list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(uid);
        PageInfo pageInfo = new PageInfo(shippings);

        return CommonReturnType.success(pageInfo);
    }
}
