package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.model.asset.metadata.Section;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author lipeng
 * @Date 2022/4/28 18:09
 * @Version 1.0
 **/
public interface SectionMapper extends BaseMapper<Section> {

    IPage<Section> selectSectionPageList(IPage<Section> sectionIPage,
                                             @Param("sectionDTO") SectionDTO sectionDTO);

    List<Section> selectAllSectionParentList(@Param("sectionIdList") List<String> sectionIdList);

    List<Section> selectSectionChildrenList(@Param("sectionId") String sectionId);

    List<Section> selectSectionListByIds(@Param("sectionIds") List<String> sectionIds);
}
