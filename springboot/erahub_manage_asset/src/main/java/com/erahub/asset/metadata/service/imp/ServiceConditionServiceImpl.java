package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.ServiceConditionConverter;
import com.erahub.asset.metadata.mapper.ServiceConditionMapper;
import com.erahub.asset.metadata.service.ServiceConditionService;
import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ServiceConditionExcel;
import com.erahub.common.model.asset.metadata.ServiceCondition;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.ServiceConditionVO;
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
import java.util.*;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:52
 * @Version 1.0
 **/
@Service
@Transactional
public class ServiceConditionServiceImpl extends ServiceImpl<ServiceConditionMapper, ServiceCondition> implements ServiceConditionService {

    private int batchSize = 1000;

    @Autowired
    private ServiceConditionMapper serviceConditionMapper;

    @Autowired
    private ServiceConditionConverter serviceConditionConverter;

    /**
     * 查询使用状态列表
     * @param serviceConditionDTO
     * @return
     */
    @Override
    public PageVO<ServiceConditionVO> getServiceConditionList(ServiceConditionDTO serviceConditionDTO) {

        IPage<ServiceCondition> serviceConditionIPage = new Page<>(serviceConditionDTO.getPageNum(), serviceConditionDTO.getPageSize());

        serviceConditionIPage = serviceConditionMapper.selectServiceConditionPageList(serviceConditionIPage, serviceConditionDTO);
        List<ServiceCondition> serviceConditionList = serviceConditionIPage.getRecords();
        List<ServiceConditionVO> serviceConditionVOS = serviceConditionConverter.converterToServiceConditionVOList(serviceConditionList);

        return new PageVO<>(serviceConditionIPage.getTotal(), serviceConditionVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<ServiceConditionExcel> exportServiceConditionExcel() {
        List<ServiceConditionExcel> serviceConditionExcels = new ArrayList<>();

        IPage<ServiceCondition> serviceConditionIPage = new Page<>();
        //临时不分页
        serviceConditionIPage.setSize(-1l);

        serviceConditionIPage = serviceConditionMapper.selectServiceConditionPageList(serviceConditionIPage, new ServiceConditionDTO());
        List<ServiceCondition> serviceConditionList = serviceConditionIPage.getRecords();
        List<ServiceConditionVO> serviceConditionVOS = serviceConditionConverter.converterToServiceConditionVOList(serviceConditionList);
        ListMapUtils.copyList(serviceConditionVOS, serviceConditionExcels, ServiceConditionExcel.class);

        return serviceConditionExcels;
    }

    /**
     * 更新使用状态状态
     *
     * @param serviceConditionId
     * @param status
     */
    @Override
    public void changeServiceConditionStatus(Long serviceConditionId, Integer status) throws AssetException {
        ServiceCondition dbServiceCondition = serviceConditionMapper.selectById(serviceConditionId);
        if (dbServiceCondition == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的使用状态不存在");
        }

        dbServiceCondition.setStatus(status);
        serviceConditionMapper.updateById(dbServiceCondition);

    }

    /**
     * 添加使用状态
     *
     * @param serviceConditionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addServiceCondition(ServiceConditionDTO serviceConditionDTO) throws AssetException {
        @NotNull(message = "使用状态id不能为空") Long serviceConditionId = serviceConditionDTO.getServiceConditionId();
        @NotBlank(message = "使用状态名称不能为空") String serviceConditionName = serviceConditionDTO.getServiceConditionName();

        QueryWrapper<ServiceCondition> serviceConditionQueryWrapper = new QueryWrapper<>();
        serviceConditionQueryWrapper.eq("service_condition_id", serviceConditionId)
                .or()
                .eq("service_condition_name", serviceConditionName);

        List<ServiceCondition> serviceConditionList = serviceConditionMapper.selectList(serviceConditionQueryWrapper);

        if (!serviceConditionList.isEmpty() && serviceConditionList.get(0).getServiceConditionId().equals(serviceConditionId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用状态id已被占用");
        }
        if (!serviceConditionList.isEmpty() && serviceConditionList.get(0).getServiceConditionName().equals(serviceConditionName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该使用状态名称已被占用");
        }

        ServiceCondition serviceCondition = new ServiceCondition();
        serviceConditionConverter.converterToServiceCondition(serviceConditionDTO, serviceCondition);

        serviceConditionMapper.insert(serviceCondition);
    }

    /**
     * 修改使用状态
     *
     * @param serviceConditionDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateServiceCondition(ServiceConditionDTO serviceConditionDTO) throws AssetException {
        @NotNull(message = "使用状态id不能为空") Long serviceConditionId = serviceConditionDTO.getServiceConditionId();

        List<ServiceCondition> updateServiceConditionList = serviceConditionMapper
                .selectList(new QueryWrapper<ServiceCondition>().like("service_condition_id", serviceConditionId));

        if (updateServiceConditionList == null || updateServiceConditionList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        ServiceCondition updateServiceCondition = updateServiceConditionList.get(0);
        serviceConditionConverter.converterToServiceCondition(serviceConditionDTO, updateServiceCondition);

        serviceConditionMapper.updateById(updateServiceCondition);
    }

    /**
     * 删除使用状态
     *
     * @param serviceConditionId
     */
    @Transactional
    @Override
    public void deleteServiceConditionById(Long serviceConditionId) throws AssetException {
        List<ServiceCondition> deleteServiceConditions = serviceConditionMapper.selectServiceConditionListByIds(Arrays.asList(serviceConditionId));

        if (deleteServiceConditions == null || deleteServiceConditions.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的使用状态不存在");
        }

        if (deleteServiceConditions.get(0).getAssetQuantity() != null
                && deleteServiceConditions.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用状态还存在资产");
        }


        serviceConditionMapper.deleteById(serviceConditionId);
    }

    /**
     * 批量删除使用状态
     *
     * @param serviceConditionIds
     */
    @Transactional
    @Override
    public void deleteServiceConditionByBatchId(List<Long> serviceConditionIds) throws AssetException {
        List<ServiceCondition> serviceConditionList = serviceConditionMapper.selectServiceConditionListByIds(serviceConditionIds);

        if (serviceConditionList != null && serviceConditionList.size() > 0) {
            for (ServiceCondition serviceCondition : serviceConditionList) {

                if (serviceCondition.getAssetQuantity() != null && serviceCondition.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的使用状态存在资产");
                }
            }
        }

        serviceConditionMapper.deleteBatchIds(serviceConditionIds);
    }

    /**
     * 上传导入使用状态
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importServiceCondition(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<ServiceCondition> serviceConditionList = new ArrayList<>();
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
                    ServiceCondition serviceCondition = new ServiceCondition();
                    Row row = sheet.getRow(i);

                    Long serviceConditionId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(serviceConditionId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(serviceConditionId);
                    }

                    //数据复值
                    serviceCondition.setServiceConditionId(serviceConditionId);
                    serviceCondition.setServiceConditionName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    serviceCondition.setAccrualDepreciation(Long.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    serviceCondition.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                    serviceCondition.setRemark(dataFormatter.formatCellValue(row.getCell(7)).trim());

                    serviceConditionList.add(serviceCondition);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<ServiceCondition> serviceConditions = serviceConditionMapper.selectBatchIds(idList);
            if (serviceConditions.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(serviceConditionList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = serviceConditionList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, serviceConditionList.get(i));
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
