package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.ReduceMethodConverter;
import com.erahub.asset.metadata.mapper.ReduceMethodMapper;
import com.erahub.asset.metadata.mapper.ReduceMethodMapper;
import com.erahub.asset.metadata.service.ReduceMethodService;
import com.erahub.common.dto.asset.metadata.ReduceMethodDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceMethodExcel;
import com.erahub.common.model.asset.metadata.ReduceMethod;
import com.erahub.common.model.asset.metadata.ReduceMethod;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.ReduceMethodVO;
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
public class ReduceMethodServiceImpl extends ServiceImpl<ReduceMethodMapper, ReduceMethod> implements ReduceMethodService {

    private int batchSize = 1000;

    @Autowired
    private ReduceMethodMapper reduceMethodMapper;

    @Autowired
    private ReduceMethodConverter reduceMethodConverter;

    /**
     * 查询减少方式列表
     * @param reduceMethodDTO
     * @return
     */
    @Override
    public PageVO<ReduceMethodVO> getReduceMethodList(ReduceMethodDTO reduceMethodDTO) {

        IPage<ReduceMethod> reduceMethodIPage = new Page<>(reduceMethodDTO.getPageNum(), reduceMethodDTO.getPageSize());

        reduceMethodIPage = reduceMethodMapper.selectReduceMethodPageList(reduceMethodIPage, reduceMethodDTO);
        List<ReduceMethod> reduceMethodList = reduceMethodIPage.getRecords();
        List<ReduceMethodVO> reduceMethodVOS = reduceMethodConverter.converterToReduceMethodVOList(reduceMethodList);

        return new PageVO<>(reduceMethodIPage.getTotal(), reduceMethodVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<ReduceMethodExcel> exportReduceMethodExcel() {
        List<ReduceMethodExcel> reduceMethodExcels = new ArrayList<>();

        IPage<ReduceMethod> reduceMethodIPage = new Page<>();
        //临时不分页
        reduceMethodIPage.setSize(-1l);

        reduceMethodIPage = reduceMethodMapper.selectReduceMethodPageList(reduceMethodIPage, new ReduceMethodDTO());
        List<ReduceMethod> reduceMethodList = reduceMethodIPage.getRecords();
        List<ReduceMethodVO> reduceMethodVOS = reduceMethodConverter.converterToReduceMethodVOList(reduceMethodList);
        ListMapUtils.copyList(reduceMethodVOS, reduceMethodExcels, ReduceMethodExcel.class);

        return reduceMethodExcels;
    }

    /**
     * 更新减少方式状态
     *
     * @param reduceMethodId
     * @param status
     */
    @Override
    public void changeReduceMethodStatus(Long reduceMethodId, Integer status) throws AssetException {
        ReduceMethod dbReduceMethod = reduceMethodMapper.selectById(reduceMethodId);
        if (dbReduceMethod == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的减少方式不存在");
        }

        dbReduceMethod.setStatus(status);
        reduceMethodMapper.updateById(dbReduceMethod);

    }

    /**
     * 添加减少方式
     *
     * @param reduceMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addReduceMethod(ReduceMethodDTO reduceMethodDTO) throws AssetException {
        @NotNull(message = "减少方式id不能为空") Long reduceMethodId = reduceMethodDTO.getReduceMethodId();
        @NotBlank(message = "减少方式名称不能为空") String reduceMethodName = reduceMethodDTO.getReduceMethodName();

        QueryWrapper<ReduceMethod> reduceMethodQueryWrapper = new QueryWrapper<>();
        reduceMethodQueryWrapper.eq("reduce_method_id", reduceMethodId)
                .or()
                .eq("reduce_method_name", reduceMethodName);

        List<ReduceMethod> reduceMethodList = reduceMethodMapper.selectList(reduceMethodQueryWrapper);

        if (!reduceMethodList.isEmpty() && reduceMethodList.get(0).getReduceMethodId().equals(reduceMethodId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少方式id已被占用");
        }
        if (!reduceMethodList.isEmpty() && reduceMethodList.get(0).getReduceMethodName().equals(reduceMethodName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少方式名称已被占用");
        }

        ReduceMethod reduceMethod = new ReduceMethod();
        reduceMethodConverter.converterToReduceMethod(reduceMethodDTO, reduceMethod);

        reduceMethodMapper.insert(reduceMethod);
    }

    /**
     * 修改减少方式
     *
     * @param reduceMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateReduceMethod(ReduceMethodDTO reduceMethodDTO) throws AssetException {
        @NotNull(message = "减少方式id不能为空") Long reduceMethodId = reduceMethodDTO.getReduceMethodId();

        List<ReduceMethod> updateReduceMethodList = reduceMethodMapper
                .selectList(new QueryWrapper<ReduceMethod>().like("reduce_method_id", reduceMethodId));

        if (updateReduceMethodList == null || updateReduceMethodList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        ReduceMethod updateReduceMethod = updateReduceMethodList.get(0);
        reduceMethodConverter.converterToReduceMethod(reduceMethodDTO, updateReduceMethod);

        reduceMethodMapper.updateById(updateReduceMethod);
    }

    /**
     * 删除减少方式
     *
     * @param reduceMethodId
     */
    @Transactional
    @Override
    public void deleteReduceMethodById(Long reduceMethodId) throws AssetException {
        List<ReduceMethod> deleteReduceMethods = reduceMethodMapper.selectReduceMethodListByIds(Arrays.asList(reduceMethodId));

        if (deleteReduceMethods == null || deleteReduceMethods.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的减少方式不存在");
        }

        if (deleteReduceMethods.get(0).getAssetQuantity() != null
                && deleteReduceMethods.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少方式还存在资产");
        }


        reduceMethodMapper.deleteById(reduceMethodId);
    }

    /**
     * 批量删除减少方式
     *
     * @param reduceMethodIds
     */
    @Transactional
    @Override
    public void deleteReduceMethodByBatchId(List<Long> reduceMethodIds) throws AssetException {
        List<ReduceMethod> reduceMethodList = reduceMethodMapper.selectReduceMethodListByIds(reduceMethodIds);

        if (reduceMethodList != null && reduceMethodList.size() > 0) {
            for (ReduceMethod reduceMethod : reduceMethodList) {

                if (reduceMethod.getAssetQuantity() != null && reduceMethod.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少方式存在资产");
                }
            }
        }

        reduceMethodMapper.deleteBatchIds(reduceMethodIds);
    }

    /**
     * 上传导入减少方式
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importReduceMethod(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<ReduceMethod> reduceMethodList = new ArrayList<>();
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
                    ReduceMethod reduceMethod = new ReduceMethod();
                    Row row = sheet.getRow(i);

                    Long reduceMethodId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(reduceMethodId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(reduceMethodId);
                    }

                    //数据复值
                    reduceMethod.setReduceMethodId(reduceMethodId);
                    reduceMethod.setReduceMethodName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    reduceMethod.setMethodMark(dataFormatter.formatCellValue(row.getCell(2)).trim());
                    reduceMethod.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                    reduceMethod.setRemark(dataFormatter.formatCellValue(row.getCell(7)).trim());

                    reduceMethodList.add(reduceMethod);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<ReduceMethod> reduceMethods = reduceMethodMapper.selectBatchIds(idList);
            if (reduceMethods.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(reduceMethodList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = reduceMethodList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, reduceMethodList.get(i));
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
