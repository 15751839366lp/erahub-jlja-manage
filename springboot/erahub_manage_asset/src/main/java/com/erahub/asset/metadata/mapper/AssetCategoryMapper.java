package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;
import com.erahub.common.model.asset.metadata.AssetCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
public interface AssetCategoryMapper extends BaseMapper<AssetCategory> {

    IPage<AssetCategory> selectAssetCategoryPageList(IPage<AssetCategory> assetCategoryIPage,
                                                          @Param("assetCategoryDTO") AssetCategoryDTO assetCategoryDTO);

    List<AssetCategory> selectAssetCategoryListByIds(@Param("assetCategoryIds") List<String> assetCategoryIds);
}
