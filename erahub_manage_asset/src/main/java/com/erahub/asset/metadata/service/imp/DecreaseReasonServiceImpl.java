package com.erahub.asset.metadata.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.DecreaseReasonConverter;
import com.erahub.asset.metadata.mapper.DecreaseReasonMapper;
import com.erahub.asset.metadata.service.DecreaseReasonService;
import com.erahub.common.dto.asset.metadata.DecreaseReasonDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseReasonExcel;
import com.erahub.common.model.asset.metadata.DecreaseReason;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.DecreaseReasonVO;
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
public class DecreaseReasonServiceImpl extends ServiceImpl<DecreaseReasonMapper, DecreaseReason> implements DecreaseReasonService {

    private int batchSize = 1000;

    @Autowired
    private DecreaseReasonMapper decreaseReasonMapper;

    @Autowired
    private DecreaseReasonConverter decreaseReasonConverter;

    /**
     * 查询减少原因列表
     * @param decreaseReasonDTO
     * @return
     */
    @Override
    public PageVO<DecreaseReasonVO> getDecreaseReasonList(DecreaseReasonDTO decreaseReasonDTO) {

        IPage<DecreaseReason> decreaseReasonIPage = new Page<>(decreaseReasonDTO.getPageNum(), decreaseReasonDTO.getPageSize());

        decreaseReasonIPage = decreaseReasonMapper.selectDecreaseReasonPageList(decreaseReasonIPage, decreaseReasonDTO);
        List<DecreaseReason> decreaseReasonList = decreaseReasonIPage.getRecords();
        List<DecreaseReasonVO> decreaseReasonVOS = decreaseReasonConverter.converterToDecreaseReasonVOList(decreaseReasonList);

        return new PageVO<>(decreaseReasonIPage.getTotal(), decreaseReasonVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<DecreaseReasonExcel> exportDecreaseReasonExcel() {
        List<DecreaseReasonExcel> decreaseReasonExcels = new ArrayList<>();

        IPage<DecreaseReason> decreaseReasonIPage = new Page<>();
        //临时不分页
        decreaseReasonIPage.setSize(-1l);

        decreaseReasonIPage = decreaseReasonMapper.selectDecreaseReasonPageList(decreaseReasonIPage, new DecreaseReasonDTO());
        List<DecreaseReason> decreaseReasonList = decreaseReasonIPage.getRecords();
        List<DecreaseReasonVO> decreaseReasonVOS = decreaseReasonConverter.converterToDecreaseReasonVOList(decreaseReasonList);
        ListMapUtils.copyList(decreaseReasonVOS, decreaseReasonExcels, DecreaseReasonExcel.class);

        return decreaseReasonExcels;
    }

    /**
     * 更新减少原因状态
     *
     * @param decreaseReasonId
     * @param status
     */
    @Override
    public void changeDecreaseReasonStatus(Long decreaseReasonId, Integer status) throws AssetException {
        DecreaseReason dbDecreaseReason = decreaseReasonMapper.selectById(decreaseReasonId);
        if (dbDecreaseReason == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的减少原因不存在");
        }

        dbDecreaseReason.setStatus(status);
        decreaseReasonMapper.updateById(dbDecreaseReason);

    }

    /**
     * 添加减少原因
     *
     * @param decreaseReasonDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addDecreaseReason(DecreaseReasonDTO decreaseReasonDTO) throws AssetException {
        @NotNull(message = "减少原因id不能为空") Long decreaseReasonId = decreaseReasonDTO.getDecreaseReasonId();
        @NotBlank(message = "减少原因名称不能为空") String decreaseReasonName = decreaseReasonDTO.getDecreaseReasonName();

        QueryWrapper<DecreaseReason> decreaseReasonQueryWrapper = new QueryWrapper<>();
        decreaseReasonQueryWrapper.eq("decrease_reason_id", decreaseReasonId)
                .or()
                .eq("decrease_reason_name", decreaseReasonName);

        List<DecreaseReason> decreaseReasonList = decreaseReasonMapper.selectList(decreaseReasonQueryWrapper);

        if (!decreaseReasonList.isEmpty() && decreaseReasonList.get(0).getDecreaseReasonId().equals(decreaseReasonId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少原因id已被占用");
        }
        if (!decreaseReasonList.isEmpty() && decreaseReasonList.get(0).getDecreaseReasonName().equals(decreaseReasonName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少原因名称已被占用");
        }

        DecreaseReason decreaseReason = new DecreaseReason();
        decreaseReasonConverter.converterToDecreaseReason(decreaseReasonDTO, decreaseReason);

        decreaseReasonMapper.insert(decreaseReason);
    }

    /**
     * 修改减少原因
     *
     * @param decreaseReasonDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateDecreaseReason(DecreaseReasonDTO decreaseReasonDTO) throws AssetException {
        @NotNull(message = "减少原因id不能为空") Long decreaseReasonId = decreaseReasonDTO.getDecreaseReasonId();

        List<DecreaseReason> updateDecreaseReasonList = decreaseReasonMapper
                .selectList(new QueryWrapper<DecreaseReason>().like("decrease_reason_id", decreaseReasonId));

        if (updateDecreaseReasonList == null || updateDecreaseReasonList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        DecreaseReason updateDecreaseReason = updateDecreaseReasonList.get(0);
        decreaseReasonConverter.converterToDecreaseReason(decreaseReasonDTO, updateDecreaseReason);

        decreaseReasonMapper.updateById(updateDecreaseReason);
    }

    /**
     * 删除减少原因
     *
     * @param decreaseReasonId
     */
    @Transactional
    @Override
    public void deleteDecreaseReasonById(Long decreaseReasonId) throws AssetException {
        List<DecreaseReason> deleteDecreaseReasons = decreaseReasonMapper.selectDecreaseReasonListByIds(Arrays.asList(decreaseReasonId));

        if (deleteDecreaseReasons == null || deleteDecreaseReasons.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的减少原因不存在");
        }

        if (deleteDecreaseReasons.get(0).getAssetQuantity() != null
                && deleteDecreaseReasons.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少原因还存在资产");
        }


        decreaseReasonMapper.deleteById(decreaseReasonId);
    }

    /**
     * 批量删除减少原因
     *
     * @param decreaseReasonIds
     */
    @Transactional
    @Override
    public void deleteDecreaseReasonByBatchId(List<Long> decreaseReasonIds) throws AssetException {
        List<DecreaseReason> decreaseReasonList = decreaseReasonMapper.selectDecreaseReasonListByIds(decreaseReasonIds);

        if (decreaseReasonList != null && decreaseReasonList.size() > 0) {
            for (DecreaseReason decreaseReason : decreaseReasonList) {

                if (decreaseReason.getAssetQuantity() != null && decreaseReason.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少原因存在资产");
                }
            }
        }

        decreaseReasonMapper.deleteBatchIds(decreaseReasonIds);
    }

    /**
     * 上传导入减少原因
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importDecreaseReason(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<DecreaseReason> decreaseReasonList = new ArrayList<>();
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
                    DecreaseReason decreaseReason = new DecreaseReason();
                    Row row = sheet.getRow(i);

                    Long decreaseReasonId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(decreaseReasonId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(decreaseReasonId);
                    }

                    //数据复值
                    decreaseReason.setDecreaseReasonId(decreaseReasonId);
                    decreaseReason.setDecreaseReasonName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    decreaseReason.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    decreaseReason.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    decreaseReasonList.add(decreaseReason);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<DecreaseReason> decreaseReasons = decreaseReasonMapper.selectBatchIds(idList);
            if (decreaseReasons.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(decreaseReasonList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = decreaseReasonList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, decreaseReasonList.get(i));
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
