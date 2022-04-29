package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
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

    /**
     * 获取单位列表
     * @param sectionDTO
     * @return
     */
    @Override
    public PageVO<SectionVO> getSectionList(SectionDTO sectionDTO) {
        IPage<Section> sectionVOIPage = new Page<>(0,-1);
        QueryWrapper<Section> sectionWrapper = new QueryWrapper<>();

        Integer isAccurate = sectionDTO.getIsAccurate();
        String sectionId = sectionDTO.getSectionId();
        String sectionName = sectionDTO.getSectionName();
        String sectionAbbreviation = sectionDTO.getSectionAbbreviation();
        Long status = sectionDTO.getStatus();
        Long sectionDetailed = sectionDTO.getSectionDetailed();
        Date startCreateTime = sectionDTO.getStartCreateTime();
        Date endCreateTime = sectionDTO.getEndCreateTime();

        if(!StringUtils.isEmpty(sectionId) && RegexUtils.isStringInteger(sectionId)){
            if (isAccurate == 0) {
                sectionWrapper.eq("section_id", sectionId);
            } else {
                sectionWrapper.like("section_id", sectionId);
            }
        }
        if(!StringUtils.isEmpty(sectionName)){
            if (isAccurate == 0) {
                sectionWrapper.eq("section_name", sectionName);
            } else {
                sectionWrapper.like("section_name", sectionName);
            }
        }
        if(!StringUtils.isEmpty(sectionAbbreviation)){
            if (isAccurate == 0) {
                sectionWrapper.eq("section_abbreviation", sectionAbbreviation);
            } else {
                sectionWrapper.like("section_abbreviation", sectionAbbreviation);
            }
        }
        if (sectionDetailed != null) {
            sectionWrapper.eq("section_detailed", sectionDetailed);
        }
        if (status != null) {
            sectionWrapper.eq("status", status);
        }
        if (startCreateTime != null) {
            sectionWrapper.ge("create_time", startCreateTime);
        }
        if (endCreateTime != null) {
            sectionWrapper.le("create_time", endCreateTime);
        }

        sectionVOIPage = sectionMapper.selectPage(sectionVOIPage, sectionWrapper);
        List<SectionVO> tree = SectionTreeBuilder.build(sectionVOIPage.getRecords());
        List<String> ids = sectionVOIPage.getRecords().stream().map(Section::getSectionId).collect(Collectors.toList());
        return new PageVO<SectionVO>(sectionMapper.selectCount(sectionWrapper),tree,ids);
    }
}
