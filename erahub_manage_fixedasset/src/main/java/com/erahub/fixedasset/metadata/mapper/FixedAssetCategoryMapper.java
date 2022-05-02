package com.erahub.fixedasset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.fixedasset.metadata.Section;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
public interface FixedAssetCategoryMapper extends BaseMapper<FixedAssetCategory> {

    IPage<FixedAssetCategory> selectFixedAssetCategoryPageList(IPage<FixedAssetCategory> fixedAssetCategoryIPage,
                                             @Param("fixedAssetCategoryDTO") FixedAssetCategoryDTO fixedAssetCategoryDTO);

    List<FixedAssetCategory> selectFixedAssetCategoryListByIds(@Param("categoryIds") List<String> sectionIds);
}
