package com.erahub.fixedasset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.fixedasset.metadata.DepreciationMethodDTO;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/21 18:22
 * @Version 1.0
 **/
public interface DepreciationMethodMapper extends BaseMapper<DepreciationMethod> {


    IPage<DepreciationMethod> selectDepreciationMethodPageList(IPage<DepreciationMethod> depreciationMethodIPage,
                                             @Param("depreciationMethodDTO") DepreciationMethodDTO depreciationMethodDTO);

    List<DepreciationMethod> selectDepreciationMethodListByIds(@Param("depreciationMethodIds") List<Long> depreciationMethodIds);
}
