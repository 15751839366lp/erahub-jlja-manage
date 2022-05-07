package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetCategoryExcel;
import com.erahub.common.model.asset.metadata.DepreciationMethod;
import com.erahub.common.model.asset.metadata.AssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.asset.metadata.converter.AssetCategoryConverter;
import com.erahub.asset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.asset.metadata.mapper.AssetCategoryMapper;
import com.erahub.asset.metadata.service.AssetCategoryService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
@Service
@Transactional
public class AssetCategoryServiceImpl extends ServiceImpl<AssetCategoryMapper, AssetCategory> implements AssetCategoryService {

    private int batchSize = 1000;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private AssetCategoryConverter assetCategoryConverter;

    @Autowired
    private DepreciationMethodMapper depreciationMethodMapper;

    /**
     * 查询资产类别列表
     *
     * @return
     */
    @Override
    public PageVO<AssetCategoryVO> getAssetCategoryList(AssetCategoryDTO assetCategoryDTO) {

        IPage<AssetCategory> assetCategoryIPage = new Page<>(assetCategoryDTO.getPageNum(), assetCategoryDTO.getPageSize());

        String assetCategoryId = assetCategoryDTO.getAssetCategoryId();

        if (!StringUtils.isEmpty(assetCategoryId) && assetCategoryId.length() % 2 != 0) {
            assetCategoryDTO.setAssetCategoryId("0" + assetCategoryDTO.getAssetCategoryId());
        }

        assetCategoryIPage = assetCategoryMapper.selectAssetCategoryPageList(assetCategoryIPage, assetCategoryDTO);
        List<AssetCategory> assetCategoryList = assetCategoryIPage.getRecords();
        List<AssetCategoryVO> assetCategoryVOS = assetCategoryConverter.converterToAssetCategoryVOList(assetCategoryList);

        return new PageVO<>(assetCategoryIPage.getTotal(), assetCategoryVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<AssetCategoryExcel> exportAssetCategoryExcel() {
        List<AssetCategoryExcel> assetCategoryExcels = new ArrayList<>();

        IPage<AssetCategory> assetCategoryIPage = new Page<>();
        //临时不分页
        assetCategoryIPage.setSize(-1l);

        assetCategoryIPage = assetCategoryMapper.selectAssetCategoryPageList(assetCategoryIPage, new AssetCategoryDTO());
        List<AssetCategory> assetCategoryList = assetCategoryIPage.getRecords();
        List<AssetCategoryVO> assetCategoryVOS = assetCategoryConverter.converterToAssetCategoryVOList(assetCategoryList);
        ListMapUtils.copyList(assetCategoryVOS, assetCategoryExcels, AssetCategoryExcel.class);

        return assetCategoryExcels;
    }

    /**
     * 更新资产类别状态
     *
     * @param assetCategoryId
     * @param status
     */
    @Override
    public void changeAssetCategoryStatus(String assetCategoryId, Long status) throws AssetException {
        AssetCategory dbAssetCategory = assetCategoryMapper.selectById(assetCategoryId);
        if (dbAssetCategory == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的资产类别不存在");
        }

        dbAssetCategory.setStatus(status);
        assetCategoryMapper.updateById(dbAssetCategory);

    }

    /**
     * 添加资产类别
     *
     * @param assetCategoryDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addAssetCategory(AssetCategoryDTO assetCategoryDTO) throws AssetException {
        @NotNull(message = "资产类别id不能为空") String assetCategoryId = assetCategoryDTO.getAssetCategoryId();
        @NotBlank(message = "资产类别名称不能为空") String assetCategoryName = assetCategoryDTO.getAssetCategoryName();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = assetCategoryDTO.getDepreciationMethodId();

        if (!StringUtils.isEmpty(assetCategoryId) && assetCategoryId.length() % 2 != 0) {
            assetCategoryId = "0" + assetCategoryId;
            assetCategoryDTO.setAssetCategoryId("0" + assetCategoryDTO.getAssetCategoryId());
        }

        QueryWrapper<AssetCategory> assetCategoryQueryWrapper = new QueryWrapper<>();
        assetCategoryQueryWrapper.eq("asset_category_id", assetCategoryId)
                .or()
                .eq("asset_category_name", assetCategoryName);

        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectList(assetCategoryQueryWrapper);

        if (!assetCategoryList.isEmpty() && assetCategoryList.get(0).getAssetCategoryId().equals(assetCategoryId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产类别id已被占用");
        }
        if (!assetCategoryList.isEmpty() && assetCategoryList.get(0).getAssetCategoryName().equals(assetCategoryName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产类别名称已被占用");
        }

        if (assetCategoryId.length() > 2) {
            AssetCategory parentAssetCategory = assetCategoryMapper.selectById(assetCategoryId.substring(0, assetCategoryId.length() - 2));
            if (parentAssetCategory == null) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产类别没有父节点");
            } else if (parentAssetCategory.getDetailed() == 1) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产类别父节点为明细节点");
            }
        }

        DepreciationMethod depreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (depreciationMethod == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该折旧方法不存在");
        }

        AssetCategory assetCategory = new AssetCategory();
        assetCategoryConverter.converterToAssetCategory(assetCategoryDTO, assetCategory);

        assetCategoryMapper.insert(assetCategory);
    }

    /**
     * 修改资产类别
     *
     * @param assetCategoryDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateAssetCategory(AssetCategoryDTO assetCategoryDTO) throws AssetException {
        @NotNull(message = "资产类别id不能为空") String categoryId = assetCategoryDTO.getAssetCategoryId();
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = assetCategoryDTO.getDepreciationMethodId();

        if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
            categoryId = "0" + categoryId;
            assetCategoryDTO.setAssetCategoryId("0" + assetCategoryDTO.getAssetCategoryId());
        }

        List<AssetCategory> updateAssetCategoryList = assetCategoryMapper
                .selectList(new QueryWrapper<AssetCategory>().likeRight("asset_category_id", categoryId));

        if (updateAssetCategoryList == null || updateAssetCategoryList.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_CATEGORY_NOT_FOUND);
        }

        if (assetCategoryDTO.getDetailed() != null
                && assetCategoryDTO.getDetailed() == 1
                && updateAssetCategoryList.size() > 1) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该节点存在子节点");
        }

        DepreciationMethod depreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (depreciationMethod == null) {
            throw new AssetException(AssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND);
        }
        AssetCategory updateAssetCategory = updateAssetCategoryList.get(0);
        assetCategoryConverter.converterToAssetCategory(assetCategoryDTO, updateAssetCategory);

        assetCategoryMapper.updateById(updateAssetCategory);
    }

    /**
     * 删除资产类别
     *
     * @param categoryId
     */
    @Transactional
    @Override
    public void deleteAssetCategoryById(String categoryId) throws AssetException {
        List<AssetCategory> deleteAssetCategories = assetCategoryMapper.selectAssetCategoryListByIds(Arrays.asList(categoryId));

        if (deleteAssetCategories == null || deleteAssetCategories.size() == 0) {
            throw new AssetException(AssetCodeEnum.SECTION_NOT_FOUND, "要删除的资产类别不存在");
        }
        if (deleteAssetCategories.get(0).getDetailed() != null
                && deleteAssetCategories.get(0).getDetailed() == 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产类别为非明细节点");
        }
        if (deleteAssetCategories.get(0).getAssetQuantity() != null
                && deleteAssetCategories.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产类别还存在资产");
        }


        assetCategoryMapper.deleteById(categoryId);
    }

    /**
     * 批量删除资产类别
     *
     * @param assetCategoryIds
     */
    @Transactional
    @Override
    public void deleteAssetCategoryByBatchId(List<String> assetCategoryIds) throws AssetException {
        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectAssetCategoryListByIds(assetCategoryIds);

        if (assetCategoryList != null && assetCategoryList.size() > 0) {
            for (AssetCategory assetCategory : assetCategoryList) {
                if (assetCategory.getDetailed() != null && assetCategory.getDetailed() == 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产类别非明细节点");
                }
                if (assetCategory.getAssetQuantity() != null && assetCategory.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产类别存在资产");
                }
            }
        }

        assetCategoryMapper.deleteBatchIds(assetCategoryIds);
    }

    /**
     * 上传导入资产类别
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAssetCategory(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        ArrayList<AssetCategory> assetCategoryList = new ArrayList<>();
        ArrayList<String> pidList = new ArrayList<>();
        HashMap<String, Long> idMap = new HashMap<>();
        DataFormatter dataFormatter = new DataFormatter();

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
                AssetCategory assetCategory = new AssetCategory();
                Row row = sheet.getRow(i);

                String categoryId = dataFormatter.formatCellValue(row.getCell(0)).trim();
                //判断ID格式
                if (!RegexUtils.isStringInteger(categoryId)) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID格式有误");
                }
                //判断ID是否省略前缀
                if (!StringUtils.isEmpty(categoryId) && categoryId.length() % 2 != 0) {
                    categoryId = "0" + categoryId;
                }
                //父节点存入list
                if (categoryId.length() > 2 && !pidList.contains(categoryId.substring(0, categoryId.length() - 2))) {
                    pidList.add(categoryId.substring(0, categoryId.length() - 2));
                }
                //ID和明细存入map
                if (idMap.containsKey(categoryId)) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                } else {
                    idMap.put(categoryId, Long.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                }

                //数据复值
                assetCategory.setAssetCategoryId(categoryId.trim());
                assetCategory.setAssetCategoryName(row.getCell(1).getStringCellValue().trim());
                assetCategory.setLevel(Long.valueOf(categoryId.length() / 2));
                assetCategory.setDetailed(Long.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                assetCategory.setStatus(Long.valueOf(dataFormatter.formatCellValue(row.getCell(5)).trim()));
                assetCategory.setDepreciationMethodId(Long.valueOf(dataFormatter.formatCellValue(row.getCell(6)).trim()));
                if (!StringUtils.isEmpty(row.getCell(8))) {
                    assetCategory.setMeasureUnit(row.getCell(8).getStringCellValue().trim());
                }

                if (!StringUtils.isEmpty(row.getCell(9))) {
                    assetCategory.setCapacityUnit(row.getCell(9).getStringCellValue().trim());
                }

                assetCategory.setDepreciationPeriod(new BigDecimal(dataFormatter.formatCellValue(row.getCell(10)).trim()));
                assetCategory.setEstimatedTotalWorkload(new BigDecimal(dataFormatter.formatCellValue(row.getCell(11)).trim()));
                assetCategory.setNetResidualValue(new BigDecimal(dataFormatter.formatCellValue(row.getCell(12)).trim()));
                assetCategory.setRemark(row.getCell(15).getStringCellValue().trim());

                assetCategoryList.add(assetCategory);
            }
        }

        //查询表中是否存在数据父节点为明细节点
        if (!pidList.isEmpty()) {
            QueryWrapper<AssetCategory> assetCategoryQueryWrapper = new QueryWrapper<>();
            assetCategoryQueryWrapper.eq("detailed", 0).in("asset_category_id", pidList);
            List<AssetCategory> assetCategories = assetCategoryMapper.selectList(assetCategoryQueryWrapper);
            List<String> collect = assetCategories.stream().map(AssetCategory::getAssetCategoryId).collect(Collectors.toList());
            //遍历查询是否存在数据父节点为明细节点
            for (AssetCategory item : assetCategoryList) {
                String pCategoryId = item.getAssetCategoryId().substring(0, item.getAssetCategoryId().length() - 2);
                if (!StringUtils.isEmpty(pCategoryId) && !collect.contains(pCategoryId)) {
                    if (idMap.containsKey(pCategoryId) && idMap.get(pCategoryId) == 1) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "父节点为明细节点");
                    } else if (!idMap.containsKey(pCategoryId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "不存在数据父节点");
                    }
                }
            }
        }

        //查询表中是否存在重复ID
        if (!idMap.isEmpty()) {
            List<AssetCategory> assetCategories = assetCategoryMapper.selectBatchIds(idMap.keySet());
            if (assetCategories.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(assetCategoryList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = assetCategoryList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, assetCategoryList.get(i));
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
