package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;

import java.util.List;


/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/

public interface FixedAssetCategoryService extends IService<FixedAssetCategory> {

    /**
     * 查询资产类别列表
     * @return
     */
    PageVO<FixedAssetCategoryVO> findFixedAssetCategoryList(FixedAssetCategoryDTO fixedAssetCategoryDTO);

    /**
     * 更新资产类别状态
     * @param categoryId
     * @param status
     */
    void changeFixedAssetCategoryStatus(Long categoryId, Boolean status);

    /**
     * 导出excel
     * @return
     */
    List<FixedAssetCategoryExcel> exportFixedAssetCategoryExcel();
}
