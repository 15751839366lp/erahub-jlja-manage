package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.AssetEconomicUseDTO;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lipeng
 * @since 2022-05-09
 */
public interface AssetEconomicUseMapper extends BaseMapper<AssetEconomicUse> {
    IPage<AssetEconomicUse> selectAssetEconomicUsePageList(IPage<AssetEconomicUse> assetEconomicUseIPage,
                                                               @Param("assetEconomicUseDTO") AssetEconomicUseDTO assetEconomicUseDTO);

    List<AssetEconomicUse> selectAssetEconomicUseListByIds(@Param("assetEconomicUseIds") List<Long> assetEconomicUseIds);
}
