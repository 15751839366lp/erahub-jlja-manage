package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.fixedasset.metadata.DepreciationMethodDTO;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.error.fixedasset.FixedAssetCodeEnum;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.DepreciationMethodExcel;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.DepreciationMethodVO;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.fixedasset.metadata.converter.DepreciationMethodConverter;
import com.erahub.fixedasset.metadata.converter.FixedAssetCategoryConverter;
import com.erahub.fixedasset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.DepreciationMethodService;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


/**
 * @Author lipeng
 * @Date 2022/4/21 18:20
 * @Version 1.0
 **/
@Service
@Transactional
public class DepreciationMethodServiceImpl extends ServiceImpl<DepreciationMethodMapper, DepreciationMethod> implements DepreciationMethodService {

    private int batchSize = 1000;

    @Autowired
    private DepreciationMethodMapper depreciationMethodMapper;

    @Autowired
    private DepreciationMethodConverter depreciationMethodConverter;

    /**
     * 查询折旧方法列表
     *
     * @return
     */
    @Override
    public PageVO<DepreciationMethodVO> getDepreciationMethodList(DepreciationMethodDTO depreciationMethodDTO) {

        IPage<DepreciationMethod> depreciationMethodIPage = new Page<>(depreciationMethodDTO.getPageNum(), depreciationMethodDTO.getPageSize());
        QueryWrapper<DepreciationMethod> depreciationMethodQueryWrapper = new QueryWrapper<>();

        Integer isAccurate = depreciationMethodDTO.getIsAccurate();
        Long depreciationMethodId = depreciationMethodDTO.getDepreciationMethodId();
        String depreciationMethodName = depreciationMethodDTO.getDepreciationMethodName();
        Long status = depreciationMethodDTO.getStatus();
        String sortColumn = depreciationMethodDTO.getSortColumn();
        Boolean isAsc = depreciationMethodDTO.getIsAsc();
        Date startCreateTime = depreciationMethodDTO.getStartCreateTime();
        Date endCreateTime = depreciationMethodDTO.getEndCreateTime();

        if (depreciationMethodId != null && RegexUtils.isInteger(String.valueOf(depreciationMethodId))) {
            if (isAccurate == 0) {
                depreciationMethodQueryWrapper.eq("depreciation_method_id", depreciationMethodId);
            } else {
                depreciationMethodQueryWrapper.like("depreciation_method_id", depreciationMethodId);
            }
        }
        if (depreciationMethodName != null) {
            if (isAccurate == 0) {
                depreciationMethodQueryWrapper.eq("depreciation_method_name", depreciationMethodName);
            } else {
                depreciationMethodQueryWrapper.like("depreciation_method_name", depreciationMethodName);
            }
        }
        if (status != null) {
            depreciationMethodQueryWrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(sortColumn)) {
            if (!isAsc) {
                depreciationMethodQueryWrapper.orderByDesc(sortColumn);
            } else {
                depreciationMethodQueryWrapper.orderByAsc(sortColumn);
            }
        }
        if (startCreateTime != null) {
            depreciationMethodQueryWrapper.ge("create_time", startCreateTime);
        }
        if (endCreateTime != null) {
            depreciationMethodQueryWrapper.le("create_time", endCreateTime);
        }

        depreciationMethodIPage = depreciationMethodMapper.selectPage(depreciationMethodIPage, depreciationMethodQueryWrapper);
        List<DepreciationMethod> depreciationMethodList = depreciationMethodIPage.getRecords();
        List<DepreciationMethodVO> depreciationMethodVOS = depreciationMethodConverter.converterToDepreciationMethodVOList(depreciationMethodList);

        return new PageVO<>(depreciationMethodIPage.getTotal(), depreciationMethodVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<DepreciationMethodExcel> exportDepreciationMethodExcel() {
        List<DepreciationMethodExcel> depreciationMethodExcels = new ArrayList<>();

        IPage<DepreciationMethod> depreciationMethodIPage = new Page<>();
        //临时不分页
        depreciationMethodIPage.setSize(-1l);

        depreciationMethodIPage = depreciationMethodMapper.selectPage(depreciationMethodIPage, new QueryWrapper<DepreciationMethod>());
        List<DepreciationMethod> depreciationMethodList = depreciationMethodIPage.getRecords();
        List<DepreciationMethodVO> fixedAssetCategoryVOS = depreciationMethodConverter.converterToDepreciationMethodVOList(depreciationMethodList);
        ListMapUtils.copyList(fixedAssetCategoryVOS, depreciationMethodExcels, DepreciationMethodExcel.class);

        return depreciationMethodExcels;
    }

    /**
     * 更新折旧方法状态
     *
     * @param depreciationMethodId
     * @param status
     */
    @Override
    public void changeDepreciationMethodStatus(Long depreciationMethodId, Long status) throws FixedAssetException {
        DepreciationMethod dbDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (dbDepreciationMethod == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "要更新状态的折旧方法不存在");
        }

        dbDepreciationMethod.setStatus(status);
        depreciationMethodMapper.updateById(dbDepreciationMethod);

    }

    /**
     * 添加折旧方法
     *
     * @param depreciationMethodDTO
     * @throws FixedAssetException
     */
    @Transactional
    @Override
    public void addDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException {
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = depreciationMethodDTO.getDepreciationMethodId();
        @NotBlank(message = "折旧方法名称不能为空") String depreciationMethodName = depreciationMethodDTO.getDepreciationMethodName();

        QueryWrapper<DepreciationMethod> depreciationMethodQueryWrapper = new QueryWrapper<>();
        depreciationMethodQueryWrapper.eq("depreciation_method_id", depreciationMethodId)
                .or()
                .eq("depreciation_method_name", depreciationMethodName);

        List<DepreciationMethod> depreciationMethodList = depreciationMethodMapper.selectList(depreciationMethodQueryWrapper);

        if (!depreciationMethodList.isEmpty() && depreciationMethodList.get(0).getDepreciationMethodId().equals(depreciationMethodId)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该折旧方法id已被占用");
        }
        if (!depreciationMethodList.isEmpty() && depreciationMethodList.get(0).getDepreciationMethodName().equals(depreciationMethodName)) {
            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "该折旧方法名称已被占用");
        }

        DepreciationMethod depreciationMethod = depreciationMethodConverter.converterToDepreciationMethod(depreciationMethodDTO);

        depreciationMethodMapper.insert(depreciationMethod);
    }

    /**
     * 修改折旧方法
     *
     * @param depreciationMethodDTO
     * @throws FixedAssetException
     */
    @Transactional
    @Override
    public void updateDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException {
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = depreciationMethodDTO.getDepreciationMethodId();

        DepreciationMethod updateDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);

        if (updateDepreciationMethod == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND);
        }

        DepreciationMethod depreciationMethod = depreciationMethodConverter.converterToDepreciationMethod(depreciationMethodDTO);

        depreciationMethodMapper.updateById(depreciationMethod);
    }

    /**
     * 删除折旧方法
     *
     * @param depreciationMethodId
     */
    @Transactional
    @Override
    public void deleteDepreciationMethod(Long depreciationMethodId) throws FixedAssetException {
        DepreciationMethod deleteDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);

        if (deleteDepreciationMethod == null) {
            throw new FixedAssetException(FixedAssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND, "要删除的折旧方法不存在");
        }

        depreciationMethodMapper.deleteById(depreciationMethodId);
    }

    /**
     * 批量删除折旧方法
     *
     * @param depreciationMethodIds
     */
    @Transactional
    @Override
    public void deleteDepreciationMethodByBatchId(List<Long> depreciationMethodIds) throws FixedAssetException {
        List<DepreciationMethod> depreciationMethodList = depreciationMethodMapper.selectBatchIds(depreciationMethodIds);

        if (depreciationMethodList == null || depreciationMethodList.size() != depreciationMethodIds.size()) {
            throw new FixedAssetException(FixedAssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND, "要删除的折旧方法不存在");
        }

        depreciationMethodMapper.deleteBatchIds(depreciationMethodIds);
    }

    /**
     * 上传导入折旧方法
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importDepreciationMethod(Map<String, MultipartFile> fileMap) {
//        Workbook workbook = null;
//        Sheet sheet = null;
//        ArrayList<FixedAssetCategory> fixedAssetCategoryList = new ArrayList<>();
//        ArrayList<String> pidList = new ArrayList<>();
//        HashMap<String, Long> idMap = new HashMap<>();
//        DataFormatter dataFormatter = new DataFormatter();
//        DecimalFormat decimalFormat = new DecimalFormat("0");
//
//        for (MultipartFile file : fileMap.values()) {
//            //判断文件是否存在
//            if (file == null || file.getName() == null) {
//                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件有误");
//            }
//            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            //判断文件类型
//            if (".xls".equals(fileType)) {
//                workbook = new HSSFWorkbook(file.getInputStream());
//                sheet = workbook.getSheetAt(0);
//            } else if (".xlsx".equals(fileType)) {
//                workbook = new XSSFWorkbook(file.getInputStream());
//                sheet = workbook.getSheetAt(0);
//            } else {
//                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "文件类型错误");
//            }
//
//            int lastRowNum = sheet.getLastRowNum();
//
//            for (int i = 1; i <= lastRowNum; i++) {
//                FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
//                Row row = sheet.getRow(i);
//
//                String categoryId = dataFormatter.formatCellValue(row.getCell(0));
//                //判断ID格式
//                if (!RegexUtils.isStringInteger(categoryId)) {
//                    throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "ID格式有误");
//                }
//                //判断ID是否省略前缀
//                if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
//                    categoryId = "0" + categoryId;
//                }
//                //父节点存入list
//                if (categoryId.length() > 2 && !pidList.contains(categoryId.substring(0, categoryId.length() - 2))) {
//                    pidList.add(categoryId.substring(0, categoryId.length() - 2));
//                }
//                //ID和明细存入map
//                if (idMap.containsKey(categoryId)) {
//                    throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
//                } else {
//                    idMap.put(categoryId, Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
//                }
//
//                //数据复值
//                fixedAssetCategory.setCategoryId(categoryId);
//                fixedAssetCategory.setCategoryName(row.getCell(1).getStringCellValue());
//                fixedAssetCategory.setCategoryLevel(Long.valueOf(decimalFormat.format(row.getCell(2).getNumericCellValue())));
//                fixedAssetCategory.setCategoryDetailed(Long.valueOf(decimalFormat.format(row.getCell(3).getNumericCellValue())));
//                fixedAssetCategory.setStatus(Long.valueOf(decimalFormat.format(row.getCell(4).getNumericCellValue())));
//                fixedAssetCategory.setDepreciationMethodId(Long.valueOf(decimalFormat.format(row.getCell(5).getNumericCellValue())));
//                if (!StringUtils.isEmpty(row.getCell(7))) {
//                    fixedAssetCategory.setMeasureUnit(row.getCell(7).getStringCellValue());
//                }
//
//                if (!StringUtils.isEmpty(row.getCell(8))) {
//                    fixedAssetCategory.setCapacityUnit(row.getCell(8).getStringCellValue());
//                }
//
//                fixedAssetCategory.setDepreciationPeriod(Long.valueOf(decimalFormat.format(row.getCell(9).getNumericCellValue() * 100)));
//                fixedAssetCategory.setEstimatedTotalWorkload(Long.valueOf(decimalFormat.format(row.getCell(10).getNumericCellValue() * 100)));
//                fixedAssetCategory.setNetResidualValue(Long.valueOf(decimalFormat.format(row.getCell(11).getNumericCellValue() * 100)));
//                fixedAssetCategory.setRemark(row.getCell(14).getStringCellValue());
//
//                fixedAssetCategoryList.add(fixedAssetCategory);
//            }
//        }
//
//        //遍历查询是否存在数据父节点为明细节点
//        for (FixedAssetCategory item : fixedAssetCategoryList) {
//            String pCategoryId = item.getCategoryId().substring(0, item.getCategoryId().length() - 2);
//            if (!StringUtils.isEmpty(pCategoryId) && idMap.containsKey(pCategoryId) && idMap.get(pCategoryId) == 1) {
//                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在数据父节点为明细节点");
//            }
//        }
//        //查询表中是否存在数据父节点为明细节点
//        if (!pidList.isEmpty()) {
//            QueryWrapper<FixedAssetCategory> fixedAssetCategoryQueryWrapper = new QueryWrapper<>();
//            fixedAssetCategoryQueryWrapper.eq("category_detailed", 0).in("category_id", pidList);
//            List<FixedAssetCategory> fixedAssetCategories = fixedAssetCategoryMapper.selectList(fixedAssetCategoryQueryWrapper);
//            if (fixedAssetCategories.size() != pidList.size()) {
//                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在数据没有父节点或父节点为明细节点");
//            }
//        }
//        //查询表中是否存在重复ID
//        if (!idMap.isEmpty()) {
//            List<FixedAssetCategory> fixedAssetCategories = fixedAssetCategoryMapper.selectBatchIds(idMap.keySet());
//            if (fixedAssetCategories.size() != 0) {
//                throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
//            }
//        }
//
//        //批量插入方法
//        if (CollectionUtils.isEmpty(fixedAssetCategoryList)) {
//            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
//        }
//        try (SqlSession batchSqlSession = sqlSessionBatch()) {
//            int size = fixedAssetCategoryList.size();
//            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
//            for (int i = 0; i < size; i++) {
//                batchSqlSession.insert(sqlStatement, fixedAssetCategoryList.get(i));
//                if (i >= 1 && i % batchSize == 0) {
//                    batchSqlSession.flushStatements();
//                }
//            }
//            batchSqlSession.flushStatements();
//        } catch (Throwable e) {
//            throw new FixedAssetException(FixedAssetCodeEnum.PARAMETER_ERROR, "表格内容有误，添加失败");
//        }
    }
}
