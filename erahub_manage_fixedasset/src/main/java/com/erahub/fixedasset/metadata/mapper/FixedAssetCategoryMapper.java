package com.erahub.fixedasset.metadata.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import org.apache.ibatis.annotations.Param;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/

public interface FixedAssetCategoryMapper extends BaseMapper<FixedAssetCategory> {

    IPage<FixedAssetCategory> selectPageList(IPage<FixedAssetCategory> fixedAssetCategoryIPage,
                                             @Param("fixedAssetCategoryDTO") FixedAssetCategoryDTO fixedAssetCategoryDTO);
}
