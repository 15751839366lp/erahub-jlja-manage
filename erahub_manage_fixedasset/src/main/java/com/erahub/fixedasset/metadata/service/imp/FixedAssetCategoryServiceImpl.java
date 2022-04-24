package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.enums.fixedasset.metadata.FixedAssetCategoryStatusEnum;
import com.erahub.common.enums.system.UserStatusEnum;
import com.erahub.common.enums.system.UserTypeEnum;
import com.erahub.common.error.fixedasset.FixedAssetCodeEnum;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.error.system.SystemCodeEnum;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.system.Department;
import com.erahub.common.model.system.User;
import com.erahub.common.model.system.UserRole;
import com.erahub.common.response.ActiveUser;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.system.UserVO;
import com.erahub.fixedasset.metadata.converter.FixedAssetCategoryConverter;
import com.erahub.fixedasset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
@Service
@Transactional
public class FixedAssetCategoryServiceImpl extends ServiceImpl<FixedAssetCategoryMapper, FixedAssetCategory> implements FixedAssetCategoryService {

    private int batchSize = 1000;

    @Autowired
    private FixedAssetCategoryMapper fixedAssetCategoryMapper;

    @Autowired
    private FixedAssetCategoryConverter fixedAssetCategoryConverter;

    @Autowired
    private DepreciationMethodMapper depreciationMethodMapper;

    /**
     * 查询资产类别列表
     *
     * @return
     */
    @Override
    public PageVO<FixedAssetCategoryVO> getFixedAssetCategoryList(FixedAssetCategoryDTO fixedAssetCategoryDTO) {

        IPage<FixedAssetCategory> fixedAssetCategoryIPage = new Page<>(fixedAssetCategoryDTO.getPageNum(), fixedAssetCategoryDTO.getPageSize());

        BigDecimal depreciationPeriod = fixedAssetCategoryDTO.getDepreciationPeriod();
        BigDecimal estimatedTotalWorkload = fixedAssetCategoryDTO.getEstimatedTotalWorkload();
        BigDecimal netResidualValue = fixedAssetCategoryDTO.getNetResidualValue();
        if (depreciationPeriod != null) {
            fixedAssetCategoryDTO.setDepreciationPeriod(ArithmeticUtils.mul(depreciationPeriod.toString(), "100"));
        }
        if (estimatedTotalWorkload != null) {
            fixedAssetCategoryDTO.setEstimatedTotalWorkload(ArithmeticUtils.mul(estimatedTotalWorkload.toString(), "100"));
        }
        if (netResidualValue != null) {
            fixedAssetCategoryDTO.setNetResidualValue(ArithmeticUtils.mul(netResidualValue.toString(), "100"));
        }


        fixedAssetCategoryIPage = fixedAssetCategoryMapper.selectPageList(fixedAssetCategoryIPage, fixedAssetCategoryDTO);
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryIPage.getRecords();
        List<FixedAssetCategoryVO> fixedAssetCategoryVOS = fixedAssetCategoryConverter.converterToFixedAssetCategoryVOList(fixedAssetCategoryList);

        return new PageVO<>(fixedAssetCategoryIPage.getTotal(), fixedAssetCategoryVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<FixedAssetCategoryExcel> exportFixedAssetCategoryExcel() {
        List<FixedAssetCategoryExcel> fixedAssetCategoryExcels = new ArrayList<>();

        IPage<FixedAssetCategory> fixedAssetCategoryIPage = new Page<>();
        //临时不分页
        fixedAssetCategoryIPage.setSize(-1l);

        fixedAssetCategoryIPage = fixedAssetCategoryMapper.selectPageList(fixedAssetCategoryIPage, new FixedAssetCategoryDTO());
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryIPage.getRecords();
        List<FixedAssetCategoryVO> fixedAssetCategoryVOS = fixedAssetCategoryConverter.converterToFixedAssetCategoryVOList(fixedAssetCategoryList);
        ListMapUtils.copyList(fixedAssetCategoryVOS, fixedAssetCategoryExcels, FixedAssetCategoryExcel.class);

        return fixedAssetCategoryExcels;
    }

    /**
     * 更新资产类别状态
     *
     * @param categoryId
     * @param status
     */
    @Override
    public void changeFixedAssetCategoryStatus(Long categoryId, Boolean status) throws FixedAssetException {
        FixedAssetCategory dbFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId);
        if (dbFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "要更新状态的资产类别不存在");
        }

        dbFixedAssetCategory.setStatus(status ? FixedAssetCategoryStatusEnum.AVAILABLE.getStatusCode() :
                FixedAssetCategoryStatusEnum.DISABLE.getStatusCode());
        fixedAssetCategoryMapper.updateById(dbFixedAssetCategory);

    }

    /**
     * 添加资产类别
     *
     * @param fixedAssetCategoryDTO
     * @throws FixedAssetException
     */
    @Transactional
    @Override
    public void addFixedAssetCategory(FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException {
        @NotNull(message = "资产类别id不能为空") Long categoryId = fixedAssetCategoryDTO.getCategoryId();
        @NotBlank(message = "资产类别名称不能为空") String categoryName = fixedAssetCategoryDTO.getCategoryName();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = fixedAssetCategoryDTO.getDepreciationMethodId();

        QueryWrapper<FixedAssetCategory> fixedAssetCategoryQueryWrapper = new QueryWrapper<>();
        fixedAssetCategoryQueryWrapper.eq("category_id", categoryId)
                .or()
                .eq("category_name", categoryName);

        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryMapper.selectList(fixedAssetCategoryQueryWrapper);
        FixedAssetCategory parentFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId / 100);

        if (categoryId / 100 != 0 && parentFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别没有父节点");
        }

        if (!fixedAssetCategoryList.isEmpty() && fixedAssetCategoryList.get(0).getCategoryId().equals(categoryId)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别id已被占用");
        }
        if (!fixedAssetCategoryList.isEmpty() && fixedAssetCategoryList.get(0).getCategoryName().equals(categoryName)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别名称已被占用");
        }
        DepreciationMethod depreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (depreciationMethod == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该折旧方法不存在");
        }

        FixedAssetCategory fixedAssetCategory = fixedAssetCategoryConverter.converterToFixedAssetCategory(fixedAssetCategoryDTO);

        fixedAssetCategoryMapper.insert(fixedAssetCategory);
    }

    /**
     * 修改资产类别
     *
     * @param fixedAssetCategoryDTO
     * @throws FixedAssetException
     */
    @Transactional
    @Override
    public void updateFixedAssetCategory(FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException {
        @NotNull(message = "资产类别id不能为空") Long categoryId = fixedAssetCategoryDTO.getCategoryId();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = fixedAssetCategoryDTO.getDepreciationMethodId();

        FixedAssetCategory updateFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId);

        if (updateFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.FIXED_ASSET_CATEGORY_NOT_FOUND);
        }

        DepreciationMethod depreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (depreciationMethod == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND);
        }
        FixedAssetCategory fixedAssetCategory = fixedAssetCategoryConverter.converterToFixedAssetCategory(fixedAssetCategoryDTO);

        fixedAssetCategoryMapper.updateById(fixedAssetCategory);
    }

    /**
     * 删除资产类别
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteFixedAssetCategoryById(Long id) throws FixedAssetException {
        FixedAssetCategory deleteFixedAssetCategory = fixedAssetCategoryMapper.selectById(id);

        if (deleteFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.FIXED_ASSET_CATEGORY_NOT_FOUND, "要删除的资产类别不存在");
        }

        fixedAssetCategoryMapper.deleteById(id);
    }

    /**
     * 批量删除资产类别
     *
     * @param categoryIds
     */
    @Transactional
    @Override
    public void deleteFixedAssetCategoryByBatchId(List<Long> categoryIds) throws FixedAssetException {
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryMapper.selectBatchIds(categoryIds);

        if (fixedAssetCategoryList == null || fixedAssetCategoryList.size() != categoryIds.size()) {
            throw new FixedAssetException(FixedAssetCodeEnum.FIXED_ASSET_CATEGORY_NOT_FOUND, "要删除的资产类别不存在");
        }

        fixedAssetCategoryMapper.deleteBatchIds(categoryIds);
    }

    /**
     * 上传导入资产类别
     *
     * @param fileMap
     */
    @Transactional
    @Override
    public void importFixedAssetCategory(Map<String, MultipartFile> fileMap) throws FixedAssetException, IOException {
        Workbook workbook = null;
        ArrayList<FixedAssetCategory> fixedAssetCategoryList = new ArrayList<>();

        for (MultipartFile file : fileMap.values()) {
            if (file == null || file.getName() == null) {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件有误");
            }
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));


            if (".xls".equals(fileType)) {

            } else if (".xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();

                DecimalFormat decimalFormat = new DecimalFormat("0");


                for (int i = 1; i <= lastRowNum; i++) {
                    FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
                    Row row = sheet.getRow(i);

                    fixedAssetCategory.setCategoryId(Long.valueOf(decimalFormat.format(row.getCell(0).getNumericCellValue())));
                    fixedAssetCategory.setCategoryName(row.getCell(1).getStringCellValue());
                    fixedAssetCategory.setCategoryLevel(Long.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
                    fixedAssetCategory.setCategoryDetailed(Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
                    fixedAssetCategory.setStatus("TRUE".equals(row.getCell(4)) ? 1l : 0l);
                    fixedAssetCategory.setDepreciationMethodId(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
                    if (!StringUtils.isEmpty(row.getCell(7))) {
                        fixedAssetCategory.setMeasureUnit(row.getCell(7).getStringCellValue());
                    }

                    if (!StringUtils.isEmpty(row.getCell(8))) {
                        fixedAssetCategory.setCapacityUnit(row.getCell(8).getStringCellValue());
                    }

                    fixedAssetCategory.setDepreciationPeriod(Long.valueOf(decimalFormat.format(row.getCell(9).getNumericCellValue() * 100)));
                    fixedAssetCategory.setEstimatedTotalWorkload(Long.valueOf(decimalFormat.format(row.getCell(10).getNumericCellValue() * 100)));
                    fixedAssetCategory.setNetResidualValue(Long.valueOf(decimalFormat.format(row.getCell(11).getNumericCellValue() * 100)));
                    fixedAssetCategory.setRemark(row.getCell(14).getStringCellValue());

                    fixedAssetCategoryList.add(fixedAssetCategory);
                }
            } else {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件类型错误");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(fixedAssetCategoryList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = fixedAssetCategoryList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, fixedAssetCategoryList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
        }
    }
}
