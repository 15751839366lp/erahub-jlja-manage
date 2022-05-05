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
     * 获取子节点
     *
     * @param sectionId
     * @return
     */
    @Override
    public PageVO<SectionVO> getChildrenList(String sectionId) throws AssetException {


        if (StringUtils.isEmpty(sectionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID为空");
        }

        List<Section> sections = sectionMapper.selectSectionChildrenList(sectionId);
        List<SectionVO> sectionVOS = sectionConverter.converterToSectionVOList(sections);

        return new PageVO<SectionVO>(sections.size(), sectionVOS);
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
        sectionVOIPage = sectionMapper.selectSectionPageList(sectionVOIPage, new SectionDTO());
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
    public void changeSectionStatus(String sectionId, Long status) throws AssetException {
        Section dbSection = sectionMapper.selectById(sectionId);
        if (dbSection == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的使用不存在");
        }

        dbSection.setStatus(status);
        sectionMapper.updateById(dbSection);
    }

    /**
     * 添加使用单位
     *
     * @param sectionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addSection(SectionDTO sectionDTO) throws AssetException {
        @NotNull(message = "使用单位id不能为空") String sectionId = sectionDTO.getSectionId();
        @NotBlank(message = "使用单位名称不能为空") String sectionName = sectionDTO.getSectionName();
        @NotNull(message = "使用单位简称不能为空") String sectionAbbreviation = sectionDTO.getSectionAbbreviation();

        Integer num = sectionMapper.selectCount(new QueryWrapper<Section>().eq("section_id", sectionId));
        if (num > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用单位id已占用");
        }

        if (sectionId.length() % 4 != 0 || !RegexUtils.isStringInteger(sectionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用单位id格式有误");
        }

        String parentSectionId = sectionId.substring(0, sectionId.length() - 4);
        List<Section> parentSection = sectionMapper.selectList(new QueryWrapper<Section>().eq("section_id", parentSectionId));
        if (!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.size() == 0)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用单位父节点不存在");
        } else if (!StringUtils.isEmpty(parentSectionId) && (parentSection == null || parentSection.get(0).getDetailed() == 1)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用单位父节点明细节点");
        }

        Section section = new Section();
        sectionConverter.converterToSection(sectionDTO, section);

        sectionMapper.insert(section);
    }

    /**
     * 修改使用单位
     *
     * @param sectionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateSection(SectionDTO sectionDTO) throws AssetException {
        @NotNull(message = "单位id不能为空") String sectionId = sectionDTO.getSectionId();

        List<Section> updateSectionList = sectionMapper
                .selectList(new QueryWrapper<Section>().likeRight("section_id", sectionId));

        if (updateSectionList == null || updateSectionList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SECTION_NOT_FOUND);
        }

        if (sectionDTO.getDetailed() != null
                && sectionDTO.getDetailed() == 1
                && updateSectionList.size() > 1) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该节点存在子节点");
        }
        Section updateSection = updateSectionList.get(0);
        sectionConverter.converterToSection(sectionDTO, updateSection);

        sectionMapper.updateById(updateSection);
    }

    /**
     * 删除使用单位
     *
     * @param sectionId
     */
    @Transactional
    @Override
    public void deleteSectionById(String sectionId) throws AssetException {
        List<Section> deleteSections = sectionMapper.selectSectionListByIds(Arrays.asList(sectionId));

        if (deleteSections == null || deleteSections.size() == 0) {
            throw new AssetException(AssetCodeEnum.SECTION_NOT_FOUND, "要删除的使用单位不存在");
        }
        if (deleteSections.get(0).getDetailed() != null
                && deleteSections.get(0).getDetailed() == 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用单位为非明细节点");
        }
        if (deleteSections.get(0).getAssetQuantity() != null
                && deleteSections.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用单位还存在资产");
        }

        sectionMapper.deleteById(sectionId);
    }

    /**
     * 批量删除使用单位
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
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用单位非明细节点");
                }
                if (section.getAssetQuantity() != null && section.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用单位存在资产");
                }
            }
        }

        sectionMapper.deleteBatchIds(sectionIds);
    }

    /**
     * 上传导入使用单位
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

        for (MultipartFile file : fileMap.values()) {
            //判断文件是否存在
            if (file == null || file.getName() == null) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "文件有误");
            }
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //判断文件类型
            if (".xls".equals(fileType)) {
                workbook = new HSSFWorkbook(file.getInputStream());
                sheet = workbook.getSheetAt(0);
            } else if (".xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(file.getInputStream());
                sheet = workbook.getSheetAt(0);
            } else {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "文件类型错误");
            }

            int lastRowNum = sheet.getLastRowNum();

            for (int i = 1; i <= lastRowNum; i++) {
                Section section = new Section();
                Row row = sheet.getRow(i);

                String sectionId = dataFormatter.formatCellValue(row.getCell(0)).trim();
                //判断ID格式
                if (!RegexUtils.isStringInteger(sectionId)) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID格式有误");
                }

                //父节点存入list
                if (sectionId.length() > 4 && !pidList.contains(sectionId.substring(0, sectionId.length() - 4))) {
                    pidList.add(sectionId.substring(0, sectionId.length() - 4));
                }
                //ID和明细存入map
                if (idMap.containsKey(sectionId)) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                } else {
                    idMap.put(sectionId, Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
                }

                //数据复值
                section.setSectionId(sectionId.trim());
                section.setSectionName(row.getCell(1).getStringCellValue().trim());
                section.setSectionAbbreviation(row.getCell(2).getStringCellValue().trim());
                section.setLevel(Long.valueOf(sectionId.length() / 4));
                section.setDetailed(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
                section.setStatus(Long.valueOf(decimalFormat.format(row.getCell(6).getNumericCellValue())));
                section.setRemark(row.getCell(9).getStringCellValue().trim());

                sectionList.add(section);
            }
        }

        //查询表中是否存在数据父节点为明细节点
        if (!pidList.isEmpty()) {
            QueryWrapper<Section> sectionQueryWrapper = new QueryWrapper<>();
            sectionQueryWrapper.eq("detailed", 0).in("section_id", pidList);
            List<Section> sections = sectionMapper.selectList(sectionQueryWrapper);
            List<String> collect = sections.stream().map(Section::getSectionId).collect(Collectors.toList());
            //遍历查询是否存在数据父节点为明细节点
            for (Section item : sectionList) {
                String pSectionId = item.getSectionId().substring(0, item.getSectionId().length() - 4);
                if (!StringUtils.isEmpty(pSectionId) && !collect.contains(pSectionId)) {
                    if (idMap.containsKey(pSectionId) && idMap.get(pSectionId) == 1) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "父节点为明细节点");
                    } else if (!idMap.containsKey(pSectionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "不存在数据父节点");
                    }
                }
            }
        }
        //查询表中是否存在重复ID
        if (!idMap.isEmpty()) {
            List<Section> sections = sectionMapper.selectBatchIds(idMap.keySet());
            if (sections.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(sectionList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
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
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容有误，添加失败");
        }
    }
}
