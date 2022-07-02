package com.erahub.business.material.converter;

import com.erahub.common.model.business.material.Product;
import com.erahub.common.vo.business.material.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/17 09:22
 * @Version 1.0
 **/
public class ProductConverter {


    /**
     * 转VOList
     * @param products
     * @return
     */
    public static   List<ProductVO> converterToVOList(List<Product> products) {
        List<ProductVO> productVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(products)){
            for (Product product : products) {
                ProductVO productVO = converterToProductVO(product);
                productVOS.add(productVO);
            }
        }
        return productVOS;
    }

    /**
     * 转VO
     * @param product
     * @return
     */
    public static ProductVO converterToProductVO(Product product) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product,productVO);
        return productVO;
    }
}
