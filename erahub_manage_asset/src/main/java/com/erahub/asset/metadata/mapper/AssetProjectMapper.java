package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.AssetProjectDTO;
import com.erahub.common.model.asset.metadata.AssetProject;
import com.erahub.common.model.asset.metadata.AssetProject;
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
public interface AssetProjectMapper extends BaseMapper<AssetProject> {
    IPage<AssetProject> selectAssetProjectPageList(IPage<AssetProject> assetProjectIPage,
                                                               @Param("assetProjectDTO") AssetProjectDTO assetProjectDTO);

    List<AssetProject> selectAssetProjectListByIds(@Param("assetProjectIds") List<Long> assetProjectIds);
}
