package com.erahub.asset.metadata.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.ReduceReasonConverter;
import com.erahub.asset.metadata.mapper.ReduceReasonMapper;
import com.erahub.asset.metadata.mapper.ReduceReasonMapper;
import com.erahub.asset.metadata.service.ReduceReasonService;
import com.erahub.common.dto.asset.metadata.ReduceReasonDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceReasonExcel;
import com.erahub.common.model.asset.metadata.ReduceReason;
import com.erahub.common.model.asset.metadata.ReduceReason;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.ReduceReasonVO;
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
public class ReduceReasonServiceImpl extends ServiceImpl<ReduceReasonMapper, ReduceReason> implements ReduceReasonService {

    private int batchSize = 1000;

    @Autowired
    private ReduceReasonMapper reduceReasonMapper;

    @Autowired
    private ReduceReasonConverter reduceReasonConverter;

    /**
     * 查询减少原因列表
     * @param reduceReasonDTO
     * @return
     */
    @Override
    public PageVO<ReduceReasonVO> getReduceReasonList(ReduceReasonDTO reduceReasonDTO) {

        IPage<ReduceReason> reduceReasonIPage = new Page<>(reduceReasonDTO.getPageNum(), reduceReasonDTO.getPageSize());

        reduceReasonIPage = reduceReasonMapper.selectReduceReasonPageList(reduceReasonIPage, reduceReasonDTO);
        List<ReduceReason> reduceReasonList = reduceReasonIPage.getRecords();
        List<ReduceReasonVO> reduceReasonVOS = reduceReasonConverter.converterToReduceReasonVOList(reduceReasonList);

        return new PageVO<>(reduceReasonIPage.getTotal(), reduceReasonVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<ReduceReasonExcel> exportReduceReasonExcel() {
        List<ReduceReasonExcel> reduceReasonExcels = new ArrayList<>();

        IPage<ReduceReason> reduceReasonIPage = new Page<>();
        //临时不分页
        reduceReasonIPage.setSize(-1l);

        reduceReasonIPage = reduceReasonMapper.selectReduceReasonPageList(reduceReasonIPage, new ReduceReasonDTO());
        List<ReduceReason> reduceReasonList = reduceReasonIPage.getRecords();
        List<ReduceReasonVO> reduceReasonVOS = reduceReasonConverter.converterToReduceReasonVOList(reduceReasonList);
        ListMapUtils.copyList(reduceReasonVOS, reduceReasonExcels, ReduceReasonExcel.class);

        return reduceReasonExcels;
    }

    /**
     * 更新减少原因状态
     *
     * @param reduceReasonId
     * @param status
     */
    @Override
    public void changeReduceReasonStatus(Long reduceReasonId, Integer status) throws AssetException {
        ReduceReason dbReduceReason = reduceReasonMapper.selectById(reduceReasonId);
        if (dbReduceReason == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的减少原因不存在");
        }

        dbReduceReason.setStatus(status);
        reduceReasonMapper.updateById(dbReduceReason);

    }

    /**
     * 添加减少原因
     *
     * @param reduceReasonDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addReduceReason(ReduceReasonDTO reduceReasonDTO) throws AssetException {
        @NotNull(message = "减少原因id不能为空") Long reduceReasonId = reduceReasonDTO.getReduceReasonId();
        @NotBlank(message = "减少原因名称不能为空") String reduceReasonName = reduceReasonDTO.getReduceReasonName();

        QueryWrapper<ReduceReason> reduceReasonQueryWrapper = new QueryWrapper<>();
        reduceReasonQueryWrapper.eq("reduce_reason_id", reduceReasonId)
                .or()
                .eq("reduce_reason_name", reduceReasonName);

        List<ReduceReason> reduceReasonList = reduceReasonMapper.selectList(reduceReasonQueryWrapper);

        if (!reduceReasonList.isEmpty() && reduceReasonList.get(0).getReduceReasonId().equals(reduceReasonId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少原因id已被占用");
        }
        if (!reduceReasonList.isEmpty() && reduceReasonList.get(0).getReduceReasonName().equals(reduceReasonName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该减少原因名称已被占用");
        }

        ReduceReason reduceReason = new ReduceReason();
        reduceReasonConverter.converterToReduceReason(reduceReasonDTO, reduceReason);

        reduceReasonMapper.insert(reduceReason);
    }

    /**
     * 修改减少原因
     *
     * @param reduceReasonDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateReduceReason(ReduceReasonDTO reduceReasonDTO) throws AssetException {
        @NotNull(message = "减少原因id不能为空") Long reduceReasonId = reduceReasonDTO.getReduceReasonId();

        List<ReduceReason> updateReduceReasonList = reduceReasonMapper
                .selectList(new QueryWrapper<ReduceReason>().like("reduce_reason_id", reduceReasonId));

        if (updateReduceReasonList == null || updateReduceReasonList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        ReduceReason updateReduceReason = updateReduceReasonList.get(0);
        reduceReasonConverter.converterToReduceReason(reduceReasonDTO, updateReduceReason);

        reduceReasonMapper.updateById(updateReduceReason);
    }

    /**
     * 删除减少原因
     *
     * @param reduceReasonId
     */
    @Transactional
    @Override
    public void deleteReduceReasonById(Long reduceReasonId) throws AssetException {
        List<ReduceReason> deleteReduceReasons = reduceReasonMapper.selectReduceReasonListByIds(Arrays.asList(reduceReasonId));

        if (deleteReduceReasons == null || deleteReduceReasons.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的减少原因不存在");
        }

        if (deleteReduceReasons.get(0).getAssetQuantity() != null
                && deleteReduceReasons.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少原因还存在资产");
        }


        reduceReasonMapper.deleteById(reduceReasonId);
    }

    /**
     * 批量删除减少原因
     *
     * @param reduceReasonIds
     */
    @Transactional
    @Override
    public void deleteReduceReasonByBatchId(List<Long> reduceReasonIds) throws AssetException {
        List<ReduceReason> reduceReasonList = reduceReasonMapper.selectReduceReasonListByIds(reduceReasonIds);

        if (reduceReasonList != null && reduceReasonList.size() > 0) {
            for (ReduceReason reduceReason : reduceReasonList) {

                if (reduceReason.getAssetQuantity() != null && reduceReason.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的减少原因存在资产");
                }
            }
        }

        reduceReasonMapper.deleteBatchIds(reduceReasonIds);
    }

    /**
     * 上传导入减少原因
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importReduceReason(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<ReduceReason> reduceReasonList = new ArrayList<>();
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
                    ReduceReason reduceReason = new ReduceReason();
                    Row row = sheet.getRow(i);

                    Long reduceReasonId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(reduceReasonId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(reduceReasonId);
                    }

                    //数据复值
                    reduceReason.setReduceReasonId(reduceReasonId);
                    reduceReason.setReduceReasonName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    reduceReason.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    reduceReason.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    reduceReasonList.add(reduceReason);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<ReduceReason> reduceReasons = reduceReasonMapper.selectBatchIds(idList);
            if (reduceReasons.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(reduceReasonList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = reduceReasonList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, reduceReasonList.get(i));
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
