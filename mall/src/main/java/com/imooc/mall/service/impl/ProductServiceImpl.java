package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.controller.vo.ProductDetailVO;
import com.imooc.mall.controller.vo.ProductVO;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.imooc.mall.enums.ProductStatusEnum.DELETE;
import static com.imooc.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * Created by Peng on 2023/6/2 15:52
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CommonReturnType list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVO> productVOList = new ArrayList<>();
        for (Product product : productList) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            productVOList.add(productVO);
        }

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVOList);
        return CommonReturnType.success(pageInfo);
    }

    @Override
    public CommonReturnType detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //这个if语句必须要这样写，而不是写！= ON_SALE，因为如果后续新增促销状态的话，会有问题
        //只对确定性条件判断
        if (product.getStatus().equals(OFF_SALE.getCode())
                || product.getStatus().equals(DELETE.getCode())) {
            return CommonReturnType.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        //敏感数据处理，修改商品库存的外显值
        productDetailVO.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return CommonReturnType.success(productDetailVO);
    }
}
