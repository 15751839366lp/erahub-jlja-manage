package com.erahub.fixedasset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.fixedasset.metadata.Section;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author lipeng
 * @Date 2022/4/28 18:09
 * @Version 1.0
 **/
public interface SectionMapper extends BaseMapper<Section> {

    IPage<Section> selectPageList(IPage<Section> sectionIPage,
                                             @Param("sectionDTO") SectionDTO sectionDTO);

    List<Section> selectAllParentList(@Param("sectionIdList") List<String> sectionIdList);

    List<Section> selectChildrenList(@Param("sectionId") String sectionId);
}
