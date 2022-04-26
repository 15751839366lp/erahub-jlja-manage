package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.error.fixedasset.FixedAssetCodeEnum;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.fixedasset.metadata.converter.FixedAssetCategoryConverter;
import com.erahub.fixedasset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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

        String categoryId = fixedAssetCategoryDTO.getCategoryId();
        BigDecimal depreciationPeriod = fixedAssetCategoryDTO.getDepreciationPeriod();
        BigDecimal estimatedTotalWorkload = fixedAssetCategoryDTO.getEstimatedTotalWorkload();
        BigDecimal netResidualValue = fixedAssetCategoryDTO.getNetResidualValue();
        if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
            fixedAssetCategoryDTO.setCategoryId("0" + fixedAssetCategoryDTO.getCategoryId());
        }
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
    public void changeFixedAssetCategoryStatus(String categoryId, Long status) throws FixedAssetException {
        FixedAssetCategory dbFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId);
        if (dbFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "要更新状态的资产类别不存在");
        }

        dbFixedAssetCategory.setStatus(status);
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
        @NotNull(message = "资产类别id不能为空") String categoryId = fixedAssetCategoryDTO.getCategoryId();
        @NotBlank(message = "资产类别名称不能为空") String categoryName = fixedAssetCategoryDTO.getCategoryName();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = fixedAssetCategoryDTO.getDepreciationMethodId();

        if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
            categoryId = "0" + categoryId;
            fixedAssetCategoryDTO.setCategoryId("0" + fixedAssetCategoryDTO.getCategoryId());
        }

        QueryWrapper<FixedAssetCategory> fixedAssetCategoryQueryWrapper = new QueryWrapper<>();
        fixedAssetCategoryQueryWrapper.eq("category_id", categoryId)
                .or()
                .eq("category_name", categoryName);

        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryMapper.selectList(fixedAssetCategoryQueryWrapper);

        if (!fixedAssetCategoryList.isEmpty() && fixedAssetCategoryList.get(0).getCategoryId().equals(categoryId)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别id已被占用");
        }
        if (!fixedAssetCategoryList.isEmpty() && fixedAssetCategoryList.get(0).getCategoryName().equals(categoryName)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别名称已被占用");
        }

        if (categoryId.length() > 2) {
            FixedAssetCategory parentFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId.substring(0, categoryId.length() - 2));
            if (parentFixedAssetCategory == null) {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别没有父节点");
            } else if (parentFixedAssetCategory.getCategoryDetailed() == 1) {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该资产类别父节点为明细节点");
            }
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
        @NotNull(message = "资产类别id不能为空") String categoryId = fixedAssetCategoryDTO.getCategoryId();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = fixedAssetCategoryDTO.getDepreciationMethodId();

        if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
            categoryId = "0" + categoryId;
            fixedAssetCategoryDTO.setCategoryId("0" + fixedAssetCategoryDTO.getCategoryId());
        }

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
     * @param categoryId
     */
    @Transactional
    @Override
    public void deleteFixedAssetCategoryById(String categoryId) throws FixedAssetException {
        FixedAssetCategory deleteFixedAssetCategory = fixedAssetCategoryMapper.selectById(categoryId);

        if (deleteFixedAssetCategory == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.FIXED_ASSET_CATEGORY_NOT_FOUND, "要删除的资产类别不存在");
        }

        fixedAssetCategoryMapper.deleteById(categoryId);
    }

    /**
     * 批量删除资产类别
     *
     * @param categoryIds
     */
    @Transactional
    @Override
    public void deleteFixedAssetCategoryByBatchId(List<String> categoryIds) throws FixedAssetException {
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importFixedAssetCategory(Map<String, MultipartFile> fileMap) throws FixedAssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        ArrayList<FixedAssetCategory> fixedAssetCategoryList = new ArrayList<>();
        ArrayList<String> pidList = new ArrayList<>();
        HashMap<String, Long> idMap = new HashMap<>();
        DataFormatter dataFormatter = new DataFormatter();
        DecimalFormat decimalFormat = new DecimalFormat("0");

        for (MultipartFile file : fileMap.values()) {
            //判断文件是否存在
            if (file == null || file.getName() == null) {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件有误");
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
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件类型错误");
            }

            int lastRowNum = sheet.getLastRowNum();

            for (int i = 1; i <= lastRowNum; i++) {
                FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
                Row row = sheet.getRow(i);

                String categoryId = dataFormatter.formatCellValue(row.getCell(0));
                //判断ID格式
                if (!RegexUtils.isStringInteger(categoryId)) {
                    throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "ID格式有误");
                }
                //判断ID是否省略前缀
                if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
                    categoryId = "0" + categoryId;
                }
                //父节点存入list
                if (categoryId.length() > 2 && !pidList.contains(categoryId.substring(0,categoryId.length() - 2))) {
                    pidList.add(categoryId.substring(0,categoryId.length() - 2));
                }
                //ID和明细存入map
                if (idMap.containsKey(categoryId)) {
                    throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                } else {
                    idMap.put(categoryId, Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
                }

                //数据复值
                fixedAssetCategory.setCategoryId(categoryId);
                fixedAssetCategory.setCategoryName(row.getCell(1).getStringCellValue());
                fixedAssetCategory.setCategoryLevel(Long.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
                fixedAssetCategory.setCategoryDetailed(Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
                fixedAssetCategory.setStatus(Long.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
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
        }

        //遍历查询是否存在数据父节点为明细节点
        for (FixedAssetCategory item : fixedAssetCategoryList) {
            String pCategoryId = item.getCategoryId().substring(0, item.getCategoryId().length() - 2);
            if (!StringUtils.isEmpty(pCategoryId) && idMap.containsKey(pCategoryId) && idMap.get(pCategoryId) == 1) {
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在数据父节点为明细节点");
            }
        }
        //查询表中是否存在数据父节点为明细节点
        if(!pidList.isEmpty() ){
            QueryWrapper<FixedAssetCategory> fixedAssetCategoryQueryWrapper = new QueryWrapper<>();
            fixedAssetCategoryQueryWrapper.eq("category_detailed",0).in("category_id",pidList);
            List<FixedAssetCategory> fixedAssetCategories = fixedAssetCategoryMapper.selectList(fixedAssetCategoryQueryWrapper);
            if(fixedAssetCategories.size() != pidList.size()){
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在数据没有父节点或父节点为明细节点");
            }
        }
        //查询表中是否存在重复ID
        if(!idMap.isEmpty() ){
            List<FixedAssetCategory> fixedAssetCategories = fixedAssetCategoryMapper.selectBatchIds(idMap.keySet());
            if(fixedAssetCategories.size() != 0){
                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(fixedAssetCategoryList)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
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
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "表格内容有误，添加失败");
        }
    }
}
