package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.PageVO;

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
}
