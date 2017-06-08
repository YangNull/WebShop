package com.scott.mmall.services.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.dao.CategoryMapper;
import com.scott.mmall.pojo.Category;
import com.scott.mmall.services.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Ubuntu on 2017/5/26.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        //TODO 验证类别结点名和这个父结点ID否已经存在，结点不允许相同，父结点不允许不存在
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("SUCCESS");
        }
        return ServerResponse.createByErrorMessage("ERROR");
    }

    /**
     * 更新一个类别结点的名称
     *
     * @param categoryName
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse updateCategory(String categoryName, Integer categoryId) {
        //TODO 验该类别是否存在
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("SUCCESS");
        }
        return ServerResponse.createByErrorMessage("ERROR");
    }

    @Override
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        List<Category> categories = categoryMapper.selectChildParallelCategory(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("not data");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildByCategoryId(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIds = Lists.newArrayList();
        if (categoryId != null) {
            for (Category category : categorySet) {
                categoryIds.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIds);
    }

    public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //一直查找下去，直到这个节点下没有孩子结点了
        List<Category> categoryList = categoryMapper.selectChildParallelCategory(category.getId());
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
