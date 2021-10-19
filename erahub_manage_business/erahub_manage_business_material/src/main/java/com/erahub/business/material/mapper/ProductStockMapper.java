package com.erahub.business.material.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erahub.common.model.business.material.ProductStock;
import com.erahub.common.vo.business.material.ProductStockVO;
import com.erahub.common.vo.business.material.ProductVO;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/21 19:38
 * @Version 1.0
 **/
public interface ProductStockMapper extends BaseMapper<ProductStock> {

    /**
     * 库存列表
     * @param productVO
     * @return
     */
    List<ProductStockVO> findProductStocks(ProductVO productVO);

    /**
     * 库存列表
     * @param productVO
     * @return
     */
    Integer findProductStocksCount(ProductVO productVO);

    /**
     * 库存信息(饼图使用)
     * @return
     */
    List<ProductStockVO> findAllStocks(ProductVO productVO);
}
