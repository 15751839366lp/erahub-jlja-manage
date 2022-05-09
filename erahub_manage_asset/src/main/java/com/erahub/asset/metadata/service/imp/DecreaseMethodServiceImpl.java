package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.DecreaseMethodConverter;
import com.erahub.asset.metadata.mapper.DecreaseMethodMapper;
import com.erahub.asset.metadata.service.DecreaseMethodService;
import com.erahub.common.dto.asset.metadata.DecreaseMethodDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseMethodExcel;
import com.erahub.common.model.asset.metadata.DecreaseMethod;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.DecreaseMethodVO;
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
public class DecreaseMethodServiceImpl extends ServiceImpl<DecreaseMethodMapper, DecreaseMethod> implements DecreaseMethodService {

    private int batchSize = 1000;

    @Autowired
    private DecreaseMethodMapper decreaseMethodMapper;

    @Autowired
    private DecreaseMethodConverter decreaseMethodConverter;

    /**
     * 查询减少方式列表
     * @param decreaseMethodDTO
     * @return
     */
    @Override
    public PageVO<DecreaseMethodVO> getDecreaseMethodList(DecreaseMethodDTO decreaseMethodDTO) {

        IPage<DecreaseMethod> decreaseMethodIPage = new Page<>(decreaseMethodDTO.getPageNum(), decreaseMethodDTO.getPageSize());

        decreaseMethodIPage = decreaseMethodMapper.selectDecreaseMethodPageList(decreaseMethodIPage, decreaseMethodDTO);
        List<DecreaseMethod> decreaseMethodList = decreaseMethodIPage.getRecords();
        List<DecreaseMethodVO> decreaseMethodVOS = decreaseMethodConverter.converterToDecreaseMethodVOList(decreaseMethodList);

        return new PageVO<>(decreaseMethodIPage.getTotal(), decreaseMethodVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<DecreaseMethodExcel> exportDecreaseMethodExcel() {
        List<DecreaseMethodExcel> decreaseMethodExcels = new ArrayList<>();

        IPage<DecreaseMethod> decreaseMethodIPage = new Page<>();
        //临时不分页
        decreaseMethodIPage.setSize(-1l);

        decreaseMethodIPage = decreaseMethodMapper.selectDecreaseMethodPageList(decreaseMethodIPage, new DecreaseMethodDTO());
        List<DecreaseMethod> decreaseMethodList = decreaseMethodIPage.getRecords();
        List<DecreaseMethodVO> decreaseMethodVOS = decreaseMethodConverter.converterToDecreaseMethodVOList(decreaseMethodList);
        ListMapUtils.copyList(decreaseMethodVOS, decreaseMethodExcels, DecreaseMethodExcel.class);

        return decreaseMethodExcels;
    }

    /**
     * 更新减少方式状态
     *
     * @param decreaseMethodId
     * @param status
     */
    @Override
    public void changeDecreaseMethodStatus(Long decreaseMethodId, Integer status) throws AssetException {
        DecreaseMethod dbDecreaseMethod = decreaseMethodMapper.selectById(decreaseMethodId);
        if (dbDecreaseMethod == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的减少方式不存在");
        }

        dbDecreaseMethod.setStatus(status);
        decreaseMethodMapper.updateById(dbDecreaseMethod);

    }

    /**
     * 添加减少方式
     *
     * @param decreaseMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addDecreaseMethod(DecreaseMethodDTO decreaseMethodDTO) throws AssetException {
        @NotNull(message = "减少方式id不能为空") Long decreaseMethodId = decreaseMethodDTO.getDecreaseMethodId();
        @NotBlank(message = "减少方式名称不能为空") String decreaseMethodName = decreaseMethodDTO.getDecreaseMethodName();

        QueryWrapper<DecreaseMethod> decreaseMethodQueryWrapper = new QueryWrapper<>();
        decreaseMethodQueryWrapper.eq("decrease_method_id", decreaseMethodId)
                .or()
                .eq("decrease_method_name", decreaseMethodName);

        List<DecreaseMethod> decreaseMethodList = decreaseMethodMapper.selectList(decreaseMethodQueryWrapper);

        if (!decreaseMethodList.isEmpty() && decreaseMethodList.get(0).getDecreaseMethodId().equals(decreaseMethodId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少方式id已被占用");
        }
        if (!decreaseMethodList.isEmpty() && decreaseMethodList.get(0).getDecreaseMethodName().equals(decreaseMethodName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少方式名称已被占用");
        }

        DecreaseMethod decreaseMethod = new DecreaseMethod();
        decreaseMethodConverter.converterToDecreaseMethod(decreaseMethodDTO, decreaseMethod);

        decreaseMethodMapper.insert(decreaseMethod);
    }

    /**
     * 修改减少方式
     *
     * @param decreaseMethodDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateDecreaseMethod(DecreaseMethodDTO decreaseMethodDTO) throws AssetException {
        @NotNull(message = "减少方式id不能为空") Long decreaseMethodId = decreaseMethodDTO.getDecreaseMethodId();

        List<DecreaseMethod> updateDecreaseMethodList = decreaseMethodMapper
                .selectList(new QueryWrapper<DecreaseMethod>().like("decrease_method_id", decreaseMethodId));

        if (updateDecreaseMethodList == null || updateDecreaseMethodList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        DecreaseMethod updateDecreaseMethod = updateDecreaseMethodList.get(0);
        decreaseMethodConverter.converterToDecreaseMethod(decreaseMethodDTO, updateDecreaseMethod);

        decreaseMethodMapper.updateById(updateDecreaseMethod);
    }

    /**
     * 删除减少方式
     *
     * @param decreaseMethodId
     */
    @Transactional
    @Override
    public void deleteDecreaseMethodById(Long decreaseMethodId) throws AssetException {
        List<DecreaseMethod> deleteDecreaseMethods = decreaseMethodMapper.selectDecreaseMethodListByIds(Arrays.asList(decreaseMethodId));

        if (deleteDecreaseMethods == null || deleteDecreaseMethods.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的减少方式不存在");
        }

        if (deleteDecreaseMethods.get(0).getAssetQuantity() != null
                && deleteDecreaseMethods.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少方式还存在资产");
        }


        decreaseMethodMapper.deleteById(decreaseMethodId);
    }

    /**
     * 批量删除减少方式
     *
     * @param decreaseMethodIds
     */
    @Transactional
    @Override
    public void deleteDecreaseMethodByBatchId(List<Long> decreaseMethodIds) throws AssetException {
        List<DecreaseMethod> decreaseMethodList = decreaseMethodMapper.selectDecreaseMethodListByIds(decreaseMethodIds);

        if (decreaseMethodList != null && decreaseMethodList.size() > 0) {
            for (DecreaseMethod decreaseMethod : decreaseMethodList) {

                if (decreaseMethod.getAssetQuantity() != null && decreaseMethod.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少方式存在资产");
                }
            }
        }

        decreaseMethodMapper.deleteBatchIds(decreaseMethodIds);
    }

    /**
     * 上传导入减少方式
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importDecreaseMethod(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<DecreaseMethod> decreaseMethodList = new ArrayList<>();
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
                    DecreaseMethod decreaseMethod = new DecreaseMethod();
                    Row row = sheet.getRow(i);

                    Long decreaseMethodId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(decreaseMethodId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(decreaseMethodId);
                    }

                    //数据复值
                    decreaseMethod.setDecreaseMethodId(decreaseMethodId);
                    decreaseMethod.setDecreaseMethodName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    decreaseMethod.setMethodMark(dataFormatter.formatCellValue(row.getCell(2)).trim());
                    decreaseMethod.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                    decreaseMethod.setRemark(dataFormatter.formatCellValue(row.getCell(7)).trim());

                    decreaseMethodList.add(decreaseMethod);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<DecreaseMethod> decreaseMethods = decreaseMethodMapper.selectBatchIds(idList);
            if (decreaseMethods.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(decreaseMethodList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = decreaseMethodList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, decreaseMethodList.get(i));
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
