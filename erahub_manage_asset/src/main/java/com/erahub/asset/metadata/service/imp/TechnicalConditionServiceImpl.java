package com.erahub.asset.metadata.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.TechnicalConditionConverter;
import com.erahub.asset.metadata.mapper.TechnicalConditionMapper;
import com.erahub.asset.metadata.mapper.TechnicalConditionMapper;
import com.erahub.asset.metadata.service.TechnicalConditionService;
import com.erahub.common.dto.asset.metadata.TechnicalConditionDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.TechnicalConditionExcel;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.TechnicalConditionVO;
import com.erahub.common.vo.common.PageVO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lipeng
 * @since 2022-05-08
 */
@Service
@Transactional
public class TechnicalConditionServiceImpl extends ServiceImpl<TechnicalConditionMapper, TechnicalCondition> implements TechnicalConditionService {

    private int batchSize = 1000;

    @Autowired
    private TechnicalConditionMapper technicalConditionMapper;

    @Autowired
    private TechnicalConditionConverter technicalConditionConverter;

    /**
     * 查询技术状态列表
     * @param technicalConditionDTO
     * @return
     */
    @Override
    public PageVO<TechnicalConditionVO> getTechnicalConditionList(TechnicalConditionDTO technicalConditionDTO) {

        IPage<TechnicalCondition> technicalConditionIPage = new Page<>(technicalConditionDTO.getPageNum(), technicalConditionDTO.getPageSize());

        technicalConditionIPage = technicalConditionMapper.selectTechnicalConditionPageList(technicalConditionIPage, technicalConditionDTO);
        List<TechnicalCondition> technicalConditionList = technicalConditionIPage.getRecords();
        List<TechnicalConditionVO> technicalConditionVOS = technicalConditionConverter.converterToTechnicalConditionVOList(technicalConditionList);

        return new PageVO<>(technicalConditionIPage.getTotal(), technicalConditionVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<TechnicalConditionExcel> exportTechnicalConditionExcel() {
        List<TechnicalConditionExcel> technicalConditionExcels = new ArrayList<>();

        IPage<TechnicalCondition> technicalConditionIPage = new Page<>();
        //临时不分页
        technicalConditionIPage.setSize(-1l);

        technicalConditionIPage = technicalConditionMapper.selectTechnicalConditionPageList(technicalConditionIPage, new TechnicalConditionDTO());
        List<TechnicalCondition> technicalConditionList = technicalConditionIPage.getRecords();
        List<TechnicalConditionVO> technicalConditionVOS = technicalConditionConverter.converterToTechnicalConditionVOList(technicalConditionList);
        ListMapUtils.copyList(technicalConditionVOS, technicalConditionExcels, TechnicalConditionExcel.class);

        return technicalConditionExcels;
    }

    /**
     * 更新技术状态状态
     *
     * @param technicalConditionId
     * @param status
     */
    @Override
    public void changeTechnicalConditionStatus(Long technicalConditionId, Integer status) throws AssetException {
        TechnicalCondition dbTechnicalCondition = technicalConditionMapper.selectById(technicalConditionId);
        if (dbTechnicalCondition == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的技术状态不存在");
        }

        dbTechnicalCondition.setStatus(status);
        technicalConditionMapper.updateById(dbTechnicalCondition);

    }

    /**
     * 添加技术状态
     *
     * @param technicalConditionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addTechnicalCondition(TechnicalConditionDTO technicalConditionDTO) throws AssetException {
        @NotNull(message = "技术状态id不能为空") Long technicalConditionId = technicalConditionDTO.getTechnicalConditionId();
        @NotBlank(message = "技术状态名称不能为空") String technicalConditionName = technicalConditionDTO.getTechnicalConditionName();

        QueryWrapper<TechnicalCondition> technicalConditionQueryWrapper = new QueryWrapper<>();
        technicalConditionQueryWrapper.eq("technical_condition_id", technicalConditionId)
                .or()
                .eq("technical_condition_name", technicalConditionName);

        List<TechnicalCondition> technicalConditionList = technicalConditionMapper.selectList(technicalConditionQueryWrapper);

        if (!technicalConditionList.isEmpty() && technicalConditionList.get(0).getTechnicalConditionId().equals(technicalConditionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该技术状态id已被占用");
        }
        if (!technicalConditionList.isEmpty() && technicalConditionList.get(0).getTechnicalConditionName().equals(technicalConditionName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该技术状态名称已被占用");
        }

        TechnicalCondition technicalCondition = new TechnicalCondition();
        technicalConditionConverter.converterToTechnicalCondition(technicalConditionDTO, technicalCondition);

        technicalConditionMapper.insert(technicalCondition);
    }

    /**
     * 修改技术状态
     *
     * @param technicalConditionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateTechnicalCondition(TechnicalConditionDTO technicalConditionDTO) throws AssetException {
        @NotNull(message = "技术状态id不能为空") Long technicalConditionId = technicalConditionDTO.getTechnicalConditionId();

        List<TechnicalCondition> updateTechnicalConditionList = technicalConditionMapper
                .selectList(new QueryWrapper<TechnicalCondition>().like("technical_condition_id", technicalConditionId));

        if (updateTechnicalConditionList == null || updateTechnicalConditionList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        TechnicalCondition updateTechnicalCondition = updateTechnicalConditionList.get(0);
        technicalConditionConverter.converterToTechnicalCondition(technicalConditionDTO, updateTechnicalCondition);

        technicalConditionMapper.updateById(updateTechnicalCondition);
    }

    /**
     * 删除技术状态
     *
     * @param technicalConditionId
     */
    @Transactional
    @Override
    public void deleteTechnicalConditionById(Long technicalConditionId) throws AssetException {
        List<TechnicalCondition> deleteTechnicalConditions = technicalConditionMapper.selectTechnicalConditionListByIds(Arrays.asList(technicalConditionId));

        if (deleteTechnicalConditions == null || deleteTechnicalConditions.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的技术状态不存在");
        }

        if (deleteTechnicalConditions.get(0).getAssetQuantity() != null
                && deleteTechnicalConditions.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的技术状态还存在资产");
        }


        technicalConditionMapper.deleteById(technicalConditionId);
    }

    /**
     * 批量删除技术状态
     *
     * @param technicalConditionIds
     */
    @Transactional
    @Override
    public void deleteTechnicalConditionByBatchId(List<Long> technicalConditionIds) throws AssetException {
        List<TechnicalCondition> technicalConditionList = technicalConditionMapper.selectTechnicalConditionListByIds(technicalConditionIds);

        if (technicalConditionList != null && technicalConditionList.size() > 0) {
            for (TechnicalCondition technicalCondition : technicalConditionList) {

                if (technicalCondition.getAssetQuantity() != null && technicalCondition.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的技术状态存在资产");
                }
            }
        }

        technicalConditionMapper.deleteBatchIds(technicalConditionIds);
    }

    /**
     * 上传导入技术状态
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importTechnicalCondition(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<TechnicalCondition> technicalConditionList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        try {
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
                    TechnicalCondition technicalCondition = new TechnicalCondition();
                    Row row = sheet.getRow(i);

                    Long technicalConditionId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(technicalConditionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(technicalConditionId);
                    }

                    //数据复值
                    technicalCondition.setTechnicalConditionId(technicalConditionId);
                    technicalCondition.setTechnicalConditionName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    technicalCondition.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    technicalCondition.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    technicalConditionList.add(technicalCondition);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<TechnicalCondition> technicalConditions = technicalConditionMapper.selectBatchIds(idList);
            if (technicalConditions.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(technicalConditionList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = technicalConditionList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, technicalConditionList.get(i));
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
