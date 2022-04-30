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
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
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
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

        Boolean isAllList = true;

        if (!StringUtils.isEmpty(sectionDTO.getSectionId())
                || !StringUtils.isEmpty(sectionDTO.getSectionName())
                || !StringUtils.isEmpty(sectionDTO.getSectionAbbreviation())
                || !StringUtils.isEmpty(sectionDTO.getSectionDetailed())
                || !StringUtils.isEmpty(sectionDTO.getStatus())
                || !StringUtils.isEmpty(sectionDTO.getStartCreateTime())
                || !StringUtils.isEmpty(sectionDTO.getEndCreateTime())
        ) {
            isAllList = false;
        }

        sectionVOIPage = sectionMapper.selectPageList(sectionVOIPage, sectionDTO);

        List<Section> sections = sectionVOIPage.getRecords();
        List<String> sectionIdList = new ArrayList<>();
        List<SectionVO> tree = new ArrayList<>();
        Integer total = sections == null ? 0 : sections.size();

        if (sections != null && sections.size() > 0) {
            sectionIdList = sections.stream().map(Section::getSectionId).collect(Collectors.toList());
            if (!isAllList) {
                sections = sectionMapper.selectAllParentList(sectionIdList);
            }

            tree = SectionTreeBuilder.build(sections);
        }

        return new PageVO<SectionVO>(total, tree, sectionIdList);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<SectionExcel> exportSectionExcel() {
        List<SectionExcel> sectionExcels = new ArrayList<>();
        IPage<Section> sectionVOIPage = new Page<>(0, -1);
        sectionVOIPage = sectionMapper.selectPageList(sectionVOIPage, new SectionDTO());
        List<SectionVO> sectionVOS = sectionConverter.converterToSectionVOList(sectionVOIPage.getRecords());
        ListMapUtils.copyList(sectionVOS, sectionExcels, SectionExcel.class);

        return sectionExcels;
    }

    /**
     * 更新使用单位状态
     *
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

    /**
     * 添加使用单位
     *
     * @param sectionDTO
     * @throws FixedAssetException
     */
    @Transactional
    @Override
    public void addSection(SectionDTO sectionDTO) throws FixedAssetException {
        @NotNull(message = "使用单位id不能为空") String sectionId = sectionDTO.getSectionId();
        @NotBlank(message = "使用单位名称不能为空") String sectionName = sectionDTO.getSectionName();
        @NotNull(message = "使用单位简称不能为空") String sectionAbbreviation = sectionDTO.getSectionAbbreviation();

        Integer num = sectionMapper.selectCount(new QueryWrapper<Section>().eq("section_id", sectionId));
        if (num > 0) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该使用单位id已占用");
        }

        if (sectionId.length() % 4 != 0 || !RegexUtils.isStringInteger(sectionId)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该使用单位id格式有误");
        }

        String parentSectionId = sectionId.substring(0, sectionId.length() - 4);
        List<Section> parentSection = sectionMapper.selectList(new QueryWrapper<Section>().eq("section_id", parentSectionId));
        if (!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.size() == 0)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该使用单位父节点不存在");
        } else if(!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.get(0).getSectionDetailed() == 1)){
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该使用单位父节点明细节点");
        }

        Section section = sectionConverter.converterToSection(sectionDTO);

        sectionMapper.insert(section);
    }
}
