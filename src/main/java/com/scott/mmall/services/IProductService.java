package com.scott.mmall.services;

import com.github.pagehelper.PageInfo;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.pojo.Product;
import com.scott.mmall.vo.ProductDetailVo;

/**
 * Created by Ubuntu on 2017/5/31.
 */
public interface IProductService {
    ServerResponse<String> saveOnUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

    ServerResponse<PageInfo> productList(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
