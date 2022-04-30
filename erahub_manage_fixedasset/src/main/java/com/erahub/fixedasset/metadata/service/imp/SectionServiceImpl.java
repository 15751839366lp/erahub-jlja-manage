package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.error.fixedasset.FixedAssetCodeEnum;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.excel.model.fixedasset.metadata.SectionExcel;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import com.erahub.fixedasset.metadata.converter.FixedAssetCategoryConverter;
import com.erahub.fixedasset.metadata.converter.SectionConverter;
import com.erahub.fixedasset.metadata.mapper.SectionMapper;
import com.erahub.fixedasset.metadata.service.SectionService;
import com.erahub.fixedasset.metadata.utils.SectionTreeBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:10
 * @Version 1.0
 **/
@Service
@Transactional
public class SectionServiceImpl extends ServiceImpl<SectionMapper, Section> implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SectionConverter sectionConverter;

    /**
     * 获取单位列表
     *
     * @param sectionDTO
     * @return
     */
    @Override
    public PageVO<SectionVO> getSectionList(SectionDTO sectionDTO) {
        IPage<Section> sectionVOIPage = new Page<>(0, -1);
        QueryWrapper<Section> sectionWrapper = new QueryWrapper<>();

        Boolean isAllList = true;
        Integer isAccurate = sectionDTO.getIsAccurate();
        String sectionId = sectionDTO.getSectionId();
        String sectionName = sectionDTO.getSectionName();
        String sectionAbbreviation = sectionDTO.getSectionAbbreviation();
        Long status = sectionDTO.getStatus();
        Long sectionDetailed = sectionDTO.getSectionDetailed();
        Date startCreateTime = sectionDTO.getStartCreateTime();
        Date endCreateTime = sectionDTO.getEndCreateTime();

        if (!StringUtils.isEmpty(sectionId)) {
            if (isAccurate == 0) {
                sectionWrapper.eq("section_id", sectionId);
            } else {
                sectionWrapper.like("section_id", sectionId);
            }
            isAllList = false;
        }
        if (!StringUtils.isEmpty(sectionName)) {
            if (isAccurate == 0) {
                sectionWrapper.eq("section_name", sectionName);
            } else {
                sectionWrapper.like("section_name", sectionName);
            }
            isAllList = false;
        }
        if (!StringUtils.isEmpty(sectionAbbreviation)) {
            if (isAccurate == 0) {
                sectionWrapper.eq("section_abbreviation", sectionAbbreviation);
            } else {
                sectionWrapper.like("section_abbreviation", sectionAbbreviation);
            }
            isAllList = false;
        }
        if (sectionDetailed != null) {
            sectionWrapper.eq("section_detailed", sectionDetailed);
            isAllList = false;
        }
        if (status != null) {
            sectionWrapper.eq("status", status);
            isAllList = false;
        }
        if (startCreateTime != null) {
            sectionWrapper.ge("create_time", startCreateTime);
            isAllList = false;
        }
        if (endCreateTime != null) {
            sectionWrapper.le("create_time", endCreateTime);
            isAllList = false;
        }

        sectionVOIPage = sectionMapper.selectPage(sectionVOIPage, sectionWrapper);

        List<Section> sections = sectionVOIPage.getRecords();
        List<String> sectionIdList = new ArrayList<>();
        List<SectionVO> tree = new ArrayList<>();
        Integer total = sections == null ? 0 : sections.size();

        if (sections != null && sections.size() > 0) {
            sectionIdList = sections.stream().map(Section::getSectionId).collect(Collectors.toList());
            if(!isAllList){
                sections = sectionMapper.selectAllParentList(sectionIdList);
            }

            tree = SectionTreeBuilder.build(sections);
        }

        return new PageVO<SectionVO>(total, tree, sectionIdList);
    }

    /**
     * 导出excel
     * @return
     */
    @Override
    public List<SectionExcel> exportSectionExcel(){
        List<SectionExcel> sectionExcels = new ArrayList<>();

        List<Section> sections = sectionMapper.selectList(new QueryWrapper<>());
        List<SectionVO> sectionVOS = sectionConverter.converterToSectionVOList(sections);
        ListMapUtils.copyList(sectionVOS, sectionExcels, SectionExcel.class);

        return sectionExcels;
    }

    /**
     * 更新使用单位状态
     * @param sectionId
     * @param status
     */
    @Override
    public void changeSectionStatus(String sectionId, Long status) throws FixedAssetException {
        Section dbSection = sectionMapper.selectById(sectionId);
        if (dbSection == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "要更新状态的使用不存在");
        }

        dbSection.setStatus(status);
        sectionMapper.updateById(dbSection);

    }
}
