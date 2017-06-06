package com.scott.mmall.services;

import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.Category;

import java.util.List;

/**
 * Created by Ubuntu on 2017/5/26.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategory(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildByCategoryId(Integer categoryId);
}
