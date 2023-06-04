package com.imooc.mall.service.impl;

import com.imooc.mall.controller.vo.CategoryVO;
import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.pojo.Category;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.imooc.mall.consts.MallConst.CATEGORY_PARENT;

/**
 * Created by Peng on 2023/6/1 21:08
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CommonReturnType selectAll() {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();
        //查出parentId=0的
        for (Category category : categories) {
            if (category.getParentId().equals(CATEGORY_PARENT)) {
                categoryVOList.add(category2CategoryVO(category));
                //对一级目录进行排序
                categoryVOList.sort((o1, o2) -> o2.getSortOrder() - o1.getSortOrder());
            }
        }
        //查出父类别下相应的子类别
        findSubCategory(categoryVOList, categories);

        return CommonReturnType.success(categoryVOList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }

    private  void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

    //查询子类别
    private void findSubCategory(List<CategoryVO> categoryVOList, List<Category> categories) {
        for (CategoryVO categoryVO : categoryVOList) {
            List<CategoryVO> subCategoryVOList = new ArrayList<>();
            //父类别id
            for (Category category : categories) {
                //设置子目录
                if (category.getParentId().equals(categoryVO.getId())) {
                    subCategoryVOList.add(category2CategoryVO(category));
                }
                //设置子目录排序
                subCategoryVOList.sort((o1, o2) -> o2.getSortOrder() - o1.getSortOrder());
                categoryVO.setSubCategories(subCategoryVOList);
                //递归，继续查询下一级目录
                findSubCategory(subCategoryVOList, categories);
            }
        }
    }

    private CategoryVO category2CategoryVO(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        return categoryVO;
    }
}
