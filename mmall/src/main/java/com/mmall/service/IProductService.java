package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by yeleichao on 2017-7-15.
 */
public interface IProductService {

     ServerResponse saveOrUpdateProduct(Product product);

     ServerResponse<String> setSaleStatus(Integer productId,Integer status);

     ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

     ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

     ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
