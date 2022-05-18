package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.dto.asset.metadata.DepreciationMethodDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DepreciationMethodExcel;
import com.erahub.common.model.asset.metadata.DepreciationMethod;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.utils.RegexUtils;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.asset.metadata.DepreciationMethodVO;
import com.erahub.asset.metadata.converter.DepreciationMethodConverter;
import com.erahub.asset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.asset.metadata.service.DepreciationMethodService;
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

        depreciationMethodIPage = depreciationMethodMapper.selectDepreciationMethodPageList(depreciationMethodIPage, depreciationMethodDTO);
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

        depreciationMethodIPage = depreciationMethodMapper.selectDepreciationMethodPageList(depreciationMethodIPage, new DepreciationMethodDTO());
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
    public void changeDepreciationMethodStatus(Long depreciationMethodId, Integer status) throws AssetException {
        DepreciationMethod dbDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);
        if (dbDepreciationMethod == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的折旧方法不存在");
        }

        dbDepreciationMethod.setStatus(status);
        depreciationMethodMapper.updateById(dbDepreciationMethod);

    }

    /**
     * 添加折旧方法
     *
     * @param depreciationMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws AssetException {
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = depreciationMethodDTO.getDepreciationMethodId();
        @NotBlank(message = "折旧方法名称不能为空") String depreciationMethodName = depreciationMethodDTO.getDepreciationMethodName();

        QueryWrapper<DepreciationMethod> depreciationMethodQueryWrapper = new QueryWrapper<>();
        depreciationMethodQueryWrapper.eq("depreciation_method_id", depreciationMethodId)
                .or()
                .eq("depreciation_method_name", depreciationMethodName);

        List<DepreciationMethod> depreciationMethodList = depreciationMethodMapper.selectList(depreciationMethodQueryWrapper);

        if (!depreciationMethodList.isEmpty() && depreciationMethodList.get(0).getDepreciationMethodId().equals(depreciationMethodId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该折旧方法id已被占用");
        }
        if (!depreciationMethodList.isEmpty() && depreciationMethodList.get(0).getDepreciationMethodName().equals(depreciationMethodName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该折旧方法名称已被占用");
        }

        DepreciationMethod depreciationMethod = new DepreciationMethod();
        depreciationMethodConverter.converterToDepreciationMethod(depreciationMethodDTO, depreciationMethod);

        depreciationMethodMapper.insert(depreciationMethod);
    }

    /**
     * 修改折旧方法
     *
     * @param depreciationMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws AssetException {
        @NotNull(message = "折旧方法id不能为空") Long depreciationMethodId = depreciationMethodDTO.getDepreciationMethodId();

        DepreciationMethod updateDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);

        if (updateDepreciationMethod == null) {
            throw new AssetException(AssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND);
        }

        depreciationMethodConverter.converterToDepreciationMethod(depreciationMethodDTO, updateDepreciationMethod);

        depreciationMethodMapper.updateById(updateDepreciationMethod);
    }

    /**
     * 删除折旧方法
     *
     * @param depreciationMethodId
     */
    @Transactional
    @Override
    public void deleteDepreciationMethod(Long depreciationMethodId) throws AssetException {
        DepreciationMethod deleteDepreciationMethod = depreciationMethodMapper.selectById(depreciationMethodId);

        if (deleteDepreciationMethod == null) {
            throw new AssetException(AssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND, "要删除的折旧方法不存在");
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
    public void deleteDepreciationMethodByBatchId(List<Long> depreciationMethodIds) throws AssetException {
        List<DepreciationMethod> depreciationMethodList = depreciationMethodMapper.selectBatchIds(depreciationMethodIds);

        if (depreciationMethodList == null || depreciationMethodList.size() != depreciationMethodIds.size()) {
            throw new AssetException(AssetCodeEnum.DEPRECIATION_METHOD_NOT_FOUND, "要删除的折旧方法不存在");
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
    public void importDepreciationMethod(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        ArrayList<DepreciationMethod> depreciationMethodList = new ArrayList<>();
        ArrayList<String> pidList = new ArrayList<>();
        HashMap<Long, String> idMap = new HashMap<>();
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
                    DepreciationMethod depreciationMethod = new DepreciationMethod();
                    Row row = sheet.getRow(i);

                    Long depreciationMethodId = new Double(row.getCell(0).getNumericCellValue()).longValue();
                    //判断ID格式
                    if (!RegexUtils.isInteger(depreciationMethodId.toString())) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "ID格式有误");
                    }

                    //ID和明细存入map
                    if (idMap.containsKey(depreciationMethodId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else if (idMap.containsValue(row.getCell(1).getStringCellValue())) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复名称");
                    } else {
                        idMap.put(depreciationMethodId, row.getCell(1).getStringCellValue());
                    }

                    //数据复值
                    depreciationMethod.setDepreciationMethodId(depreciationMethodId);
                    depreciationMethod.setDepreciationMethodName(row.getCell(1).getStringCellValue());
                    depreciationMethod.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));

                    if (!StringUtils.isEmpty(row.getCell(5))) {
                        depreciationMethod.setFormula(row.getCell(5).getStringCellValue());
                    }
                    if (!StringUtils.isEmpty(row.getCell(6))) {
                        depreciationMethod.setFormulaExplain(row.getCell(6).getStringCellValue());
                    }
                    if (!StringUtils.isEmpty(row.getCell(9))) {
                        depreciationMethod.setRemark(row.getCell(9).getStringCellValue());
                    }

                    depreciationMethodList.add(depreciationMethod);
                }
            }

        } catch (Exception e) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idMap.isEmpty()) {
            List<DepreciationMethod> depreciationMethods = depreciationMethodMapper.selectBatchIds(idMap.keySet());
            if (depreciationMethods.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(depreciationMethodList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = depreciationMethodList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, depreciationMethodList.get(i));
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
