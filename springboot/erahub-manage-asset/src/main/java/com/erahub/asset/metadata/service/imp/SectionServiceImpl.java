package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.SectionExcel;
import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.asset.metadata.SectionVO;
import com.erahub.asset.metadata.converter.SectionConverter;
import com.erahub.asset.metadata.mapper.SectionMapper;
import com.erahub.asset.metadata.service.SectionService;
import com.erahub.asset.metadata.utils.SectionTreeBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:10
 * @Version 1.0
 **/
@Service
@Transactional
public class SectionServiceImpl extends ServiceImpl<SectionMapper, Section> implements SectionService {

    private int batchSize = 1000;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SectionConverter sectionConverter;

    /**
     * ??????????????????
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
                || !StringUtils.isEmpty(sectionDTO.getDetailed())
                || !StringUtils.isEmpty(sectionDTO.getStatus())
                || !StringUtils.isEmpty(sectionDTO.getStartCreateTime())
                || !StringUtils.isEmpty(sectionDTO.getEndCreateTime())
        ) {
            isAllList = false;
        }

        sectionVOIPage = sectionMapper.selectSectionPageList(sectionVOIPage, sectionDTO);

        List<Section> sections = sectionVOIPage.getRecords();
        List<String> sectionIdList = new ArrayList<>();
        List<SectionVO> tree = new ArrayList<>();
        Integer total = sections == null ? 0 : sections.size();

        if (sections != null && sections.size() > 0) {
            sectionIdList = sections.stream().map(Section::getSectionId).collect(Collectors.toList());
            if (!isAllList) {
                sections = sectionMapper.selectAllSectionParentList(sectionIdList);
            }

            tree = SectionTreeBuilder.build(sections);
        }

        return new PageVO<SectionVO>(total, tree, sectionIdList);
    }

    /**
     * ???????????????
     *
     * @param sectionId
     * @return
     */
    @Override
    public PageVO<SectionVO> getChildrenList(String sectionId) throws AssetException {


        if (StringUtils.isEmpty(sectionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID??????");
        }

        List<Section> sections = sectionMapper.selectSectionChildrenList(sectionId);
        List<SectionVO> sectionVOS = sectionConverter.converterToSectionVOList(sections);

        return new PageVO<SectionVO>(sections.size(), sectionVOS);
    }

    /**
     * ??????excel
     *
     * @return
     */
    @Override
    public List<SectionExcel> exportSectionExcel() {
        List<SectionExcel> sectionExcels = new ArrayList<>();
        IPage<Section> sectionVOIPage = new Page<>(0, -1);
        sectionVOIPage = sectionMapper.selectSectionPageList(sectionVOIPage, new SectionDTO());
        List<SectionVO> sectionVOS = sectionConverter.converterToSectionVOList(sectionVOIPage.getRecords());
        ListMapUtils.copyList(sectionVOS, sectionExcels, SectionExcel.class);

        return sectionExcels;
    }

    /**
     * ????????????????????????
     *
     * @param sectionId
     * @param status
     */
    @Override
    public void changeSectionStatus(String sectionId, Integer status) throws AssetException {
        Section dbSection = sectionMapper.selectById(sectionId);
        if (dbSection == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "?????????????????????????????????");
        }

        dbSection.setStatus(status);
        sectionMapper.updateById(dbSection);
    }

    /**
     * ??????????????????
     *
     * @param sectionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addSection(SectionDTO sectionDTO) throws AssetException {
        @NotNull(message = "????????????id????????????") String sectionId = sectionDTO.getSectionId();
        @NotBlank(message = "??????????????????????????????") String sectionName = sectionDTO.getSectionName();
        @NotNull(message = "??????????????????????????????") String sectionAbbreviation = sectionDTO.getSectionAbbreviation();

        Integer num = sectionMapper.selectCount(new QueryWrapper<Section>().eq("section_id", sectionId));
        if (num > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "???????????????id?????????");
        }

        if (sectionId.length() % 4 != 0 || !RegexUtils.isStringInteger(sectionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "???????????????id????????????");
        }

        String parentSectionId = sectionId.substring(0, sectionId.length() - 4);
        List<Section> parentSection = sectionMapper.selectList(new QueryWrapper<Section>().eq("section_id", parentSectionId));
        if (!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.size() == 0)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "?????????????????????????????????");
        } else if (!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.get(0).getDetailed() == 1)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????????????????????????????");
        }

        Section section = new Section();
        sectionConverter.converterToSection(sectionDTO, section);

        sectionMapper.insert(section);
    }

    /**
     * ??????????????????
     *
     * @param sectionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateSection(SectionDTO sectionDTO) throws AssetException {
        @NotNull(message = "??????id????????????") String sectionId = sectionDTO.getSectionId();

        List<Section> updateSectionList = sectionMapper
                .selectList(new QueryWrapper<Section>().likeRight("section_id", sectionId));

        if (updateSectionList == null || updateSectionList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SECTION_NOT_FOUND);
        }

        if (sectionDTO.getDetailed() != null
                && sectionDTO.getDetailed() == 1
                && updateSectionList.size() > 1) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????????????????");
        }
        Section updateSection = updateSectionList.get(0);
        sectionConverter.converterToSection(sectionDTO, updateSection);

        sectionMapper.updateById(updateSection);
    }

    /**
     * ??????????????????
     *
     * @param sectionId
     */
    @Transactional
    @Override
    public void deleteSectionById(String sectionId) throws AssetException {
        List<Section> deleteSections = sectionMapper.selectSectionListByIds(Arrays.asList(sectionId));

        if (deleteSections == null || deleteSections.size() == 0) {
            throw new AssetException(AssetCodeEnum.SECTION_NOT_FOUND, "?????????????????????????????????");
        }
        if (deleteSections.get(0).getDetailed() != null
                && deleteSections.get(0).getDetailed() == 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "??????????????????????????????????????????");
        }
        if (deleteSections.get(0).getAssetQuantity() != null
                && deleteSections.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "???????????????????????????????????????");
        }

        sectionMapper.deleteById(sectionId);
    }

    /**
     * ????????????????????????
     *
     * @param sectionIds
     */
    @Transactional
    @Override
    public void deleteSectionByBatchId(List<String> sectionIds) throws AssetException {
        List<Section> sectionList = sectionMapper.selectSectionListByIds(sectionIds);

        if (sectionList != null && sectionList.size() > 0) {
            for (Section section : sectionList) {
                if (section.getDetailed() != null && section.getDetailed() == 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "???????????????????????????????????????");
                }
                if (section.getAssetQuantity() != null && section.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????????????????????????????");
                }
            }
        }

        sectionMapper.deleteBatchIds(sectionIds);
    }

    /**
     * ????????????????????????
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importSection(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        ArrayList<Section> sectionList = new ArrayList<>();
        ArrayList<String> pidList = new ArrayList<>();
        HashMap<String, Long> idMap = new HashMap<>();
        DataFormatter dataFormatter = new DataFormatter();
        DecimalFormat decimalFormat = new DecimalFormat("0");

        try {
            for (MultipartFile file : fileMap.values()) {
                //????????????????????????
                if (file == null || file.getName() == null) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????");
                }
                String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                //??????????????????
                if (".xls".equals(fileType)) {
                    workbook = new HSSFWorkbook(file.getInputStream());
                    sheet = workbook.getSheetAt(0);
                } else if (".xlsx".equals(fileType)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                    sheet = workbook.getSheetAt(0);
                } else {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "??????????????????");
                }

                int lastRowNum = sheet.getLastRowNum();

                for (int i = 1; i <= lastRowNum; i++) {
                    Section section = new Section();
                    Row row = sheet.getRow(i);

                    String sectionId = dataFormatter.formatCellValue(row.getCell(0)).trim();
                    //??????ID??????
                    if (!RegexUtils.isStringInteger(sectionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID????????????");
                    }

                    //???????????????list
                    if (sectionId.length() > 4 && !pidList.contains(sectionId.substring(0, sectionId.length() - 4))) {
                        pidList.add(sectionId.substring(0, sectionId.length() - 4));
                    }
                    //ID???????????????map
                    if (idMap.containsKey(sectionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????ID");
                    } else {
                        idMap.put(sectionId, Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
                    }

                    //????????????
                    section.setSectionId(sectionId.trim());
                    section.setSectionName(row.getCell(1).getStringCellValue().trim());
                    section.setSectionAbbreviation(row.getCell(2).getStringCellValue().trim());
                    section.setLevel(Integer.valueOf(sectionId.length() / 4));
                    section.setDetailed(Integer.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
                    section.setStatus(Integer.valueOf(decimalFormat.format(row.getCell(6).getNumericCellValue())));
                    section.setRemark(row.getCell(9).getStringCellValue().trim());

                    sectionList.add(section);
                }
            }

        } catch (Exception e) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //??????????????????????????????????????????????????????
        if (!pidList.isEmpty()) {
            QueryWrapper<Section> sectionQueryWrapper = new QueryWrapper<>();
            sectionQueryWrapper.eq("detailed", 0).in("section_id", pidList);
            List<Section> sections = sectionMapper.selectList(sectionQueryWrapper);
            List<String> collect = sections.stream().map(Section::getSectionId).collect(Collectors.toList());
            //??????????????????????????????????????????????????????
            for (Section item : sectionList) {
                String pSectionId = item.getSectionId().substring(0, item.getSectionId().length() - 4);
                if (!StringUtils.isEmpty(pSectionId) && !collect.contains(pSectionId)) {
                    if (idMap.containsKey(pSectionId) && idMap.get(pSectionId) == 1) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????????????????");
                    } else if (!idMap.containsKey(pSectionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????????????????");
                    }
                }
            }
        }
        //??????????????????????????????ID
        if (!idMap.isEmpty()) {
            List<Section> sections = sectionMapper.selectBatchIds(idMap.keySet());
            if (sections.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "????????????ID");
            }
        }

        //??????????????????
        if (CollectionUtils.isEmpty(sectionList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "??????????????????");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = sectionList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, sectionList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "?????????????????????????????????");
        }
    }
}
