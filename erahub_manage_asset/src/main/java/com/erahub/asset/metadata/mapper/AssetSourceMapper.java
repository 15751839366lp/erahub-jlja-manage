package com.erahub.asset.metadata.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.AssetSourceDTO;
import com.erahub.common.model.asset.metadata.AssetSource;
import com.erahub.common.model.asset.metadata.AssetSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lipeng
 * @since 2022-05-08
 */
public interface AssetSourceMapper extends BaseMapper<AssetSource> {
    IPage<AssetSource> selectAssetSourcePageList(IPage<AssetSource> assetSourceIPage,
                                                           @Param("assetSourceDTO") AssetSourceDTO assetSourceDTO);

    List<AssetSource> selectAssetSourceListByIds(@Param("assetSourceIds") List<Long> assetSourceIds);
}
