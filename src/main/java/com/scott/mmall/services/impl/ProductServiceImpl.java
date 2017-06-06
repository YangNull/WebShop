package com.scott.mmall.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.scott.mmall.common.ServerResponse;
import com.scott.mmall.dao.CategoryMapper;
import com.scott.mmall.dao.ProductMapper;
import com.scott.mmall.pojo.Category;
import com.scott.mmall.pojo.Product;
import com.scott.mmall.services.IProductService;
import com.scott.mmall.util.DateTimeUtil;
import com.scott.mmall.util.PropertiesUtil;
import com.scott.mmall.vo.ProductDetailVo;
import com.scott.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Ubuntu on 2017/5/28.
 */
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> saveOnUpdataProduct(Product product) {
        if (product.getId() == null) {
            //save
            if (!StringUtils.isBlank(product.getSubImages())) {
                String images[] = product.getSubImages().split(",");
                if (images.length > 0) {
                    product.setMainImage(images[0]);
                }
            }
            int rowCount = productMapper.insert(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("保存成功");
            }
            return ServerResponse.createByErrorMessage("保存失败");
        } else {
            //update
            if (productMapper.checkById(product.getId()) > 0) {
                return ServerResponse.createByErrorMessage("不存在的商品");
            }
            int rowCont = productMapper.updateByPrimaryKeySelective(product);
            if (rowCont > 0) {
                return ServerResponse.createBySuccessMessage("更新成功");
            }
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productMapper.checkById(productId) > 0) {
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("商品状态更新成功");
            } else {
                return ServerResponse.createByErrorMessage("商品状态更新失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("不存在的商品");
        }

    }

    private ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已经下架");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }

    /**
     * POJO->VO
     *
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);//默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServerResponse<PageInfo> productList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectToList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        int len = productList.size();
        for (int i = 0; i < len; i++) {
            ProductListVo productListVo = assembleProductListVo(productList.get(i));
            productListVos.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);


    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        int len = productList.size();
        for (int i = 0; i < len; i++) {
            ProductListVo productListVo = assembleProductListVo(productList.get(i));
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.yangkangjian.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
