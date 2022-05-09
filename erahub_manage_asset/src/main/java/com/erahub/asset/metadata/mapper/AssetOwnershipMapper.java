package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.AssetOwnershipDTO;
import com.erahub.common.model.asset.metadata.AssetOwnership;
import com.erahub.common.model.asset.metadata.AssetOwnership;
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
public interface AssetOwnershipMapper extends BaseMapper<AssetOwnership> {
    IPage<AssetOwnership> selectAssetOwnershipPageList(IPage<AssetOwnership> assetOwnershipIPage,
                                                               @Param("assetOwnershipDTO") AssetOwnershipDTO assetOwnershipDTO);

    List<AssetOwnership> selectAssetOwnershipListByIds(@Param("assetOwnershipIds") List<Long> assetOwnershipIds);
}
