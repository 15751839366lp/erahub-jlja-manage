package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.AssetSourceConverter;
import com.erahub.asset.metadata.mapper.AssetSourceMapper;
import com.erahub.asset.metadata.mapper.AssetSourceMapper;
import com.erahub.asset.metadata.service.AssetSourceService;
import com.erahub.common.dto.asset.metadata.AssetSourceDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetSourceExcel;
import com.erahub.common.model.asset.metadata.AssetSource;
import com.erahub.common.model.asset.metadata.AssetSource;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.AssetSourceVO;
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
public class AssetSourceServiceImpl extends ServiceImpl<AssetSourceMapper, AssetSource> implements AssetSourceService {

    private int batchSize = 1000;

    @Autowired
    private AssetSourceMapper assetSourceMapper;

    @Autowired
    private AssetSourceConverter assetSourceConverter;

    /**
     * 查询资产来源列表
     * @param assetSourceDTO
     * @return
     */
    @Override
    public PageVO<AssetSourceVO> getAssetSourceList(AssetSourceDTO assetSourceDTO) {

        IPage<AssetSource> assetSourceIPage = new Page<>(assetSourceDTO.getPageNum(), assetSourceDTO.getPageSize());

        assetSourceIPage = assetSourceMapper.selectAssetSourcePageList(assetSourceIPage, assetSourceDTO);
        List<AssetSource> assetSourceList = assetSourceIPage.getRecords();
        List<AssetSourceVO> assetSourceVOS = assetSourceConverter.converterToAssetSourceVOList(assetSourceList);

        return new PageVO<>(assetSourceIPage.getTotal(), assetSourceVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<AssetSourceExcel> exportAssetSourceExcel() {
        List<AssetSourceExcel> assetSourceExcels = new ArrayList<>();

        IPage<AssetSource> assetSourceIPage = new Page<>();
        //临时不分页
        assetSourceIPage.setSize(-1l);

        assetSourceIPage = assetSourceMapper.selectAssetSourcePageList(assetSourceIPage, new AssetSourceDTO());
        List<AssetSource> assetSourceList = assetSourceIPage.getRecords();
        List<AssetSourceVO> assetSourceVOS = assetSourceConverter.converterToAssetSourceVOList(assetSourceList);
        ListMapUtils.copyList(assetSourceVOS, assetSourceExcels, AssetSourceExcel.class);

        return assetSourceExcels;
    }

    /**
     * 更新资产来源状态
     *
     * @param assetSourceId
     * @param status
     */
    @Override
    public void changeAssetSourceStatus(Long assetSourceId, Integer status) throws AssetException {
        AssetSource dbAssetSource = assetSourceMapper.selectById(assetSourceId);
        if (dbAssetSource == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的资产来源不存在");
        }

        dbAssetSource.setStatus(status);
        assetSourceMapper.updateById(dbAssetSource);

    }

    /**
     * 添加资产来源
     *
     * @param assetSourceDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addAssetSource(AssetSourceDTO assetSourceDTO) throws AssetException {
        @NotNull(message = "资产来源id不能为空") Long assetSourceId = assetSourceDTO.getAssetSourceId();
        @NotBlank(message = "资产来源名称不能为空") String assetSourceName = assetSourceDTO.getAssetSourceName();

        QueryWrapper<AssetSource> assetSourceQueryWrapper = new QueryWrapper<>();
        assetSourceQueryWrapper.eq("asset_source_id", assetSourceId)
                .or()
                .eq("asset_source_name", assetSourceName);

        List<AssetSource> assetSourceList = assetSourceMapper.selectList(assetSourceQueryWrapper);

        if (!assetSourceList.isEmpty() && assetSourceList.get(0).getAssetSourceId().equals(assetSourceId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产来源id已被占用");
        }
        if (!assetSourceList.isEmpty() && assetSourceList.get(0).getAssetSourceName().equals(assetSourceName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该资产来源名称已被占用");
        }

        AssetSource assetSource = new AssetSource();
        assetSourceConverter.converterToAssetSource(assetSourceDTO, assetSource);

        assetSourceMapper.insert(assetSource);
    }

    /**
     * 修改资产来源
     *
     * @param assetSourceDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateAssetSource(AssetSourceDTO assetSourceDTO) throws AssetException {
        @NotNull(message = "资产来源id不能为空") Long assetSourceId = assetSourceDTO.getAssetSourceId();

        List<AssetSource> updateAssetSourceList = assetSourceMapper
                .selectList(new QueryWrapper<AssetSource>().like("asset_source_id", assetSourceId));

        if (updateAssetSourceList == null || updateAssetSourceList.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND);
        }

        AssetSource updateAssetSource = updateAssetSourceList.get(0);
        assetSourceConverter.converterToAssetSource(assetSourceDTO, updateAssetSource);

        assetSourceMapper.updateById(updateAssetSource);
    }

    /**
     * 删除资产来源
     *
     * @param assetSourceId
     */
    @Transactional
    @Override
    public void deleteAssetSourceById(Long assetSourceId) throws AssetException {
        List<AssetSource> deleteAssetSources = assetSourceMapper.selectAssetSourceListByIds(Arrays.asList(assetSourceId));

        if (deleteAssetSources == null || deleteAssetSources.size() == 0) {
            throw new AssetException(AssetCodeEnum.SERVICE_CONDITION_NOT_FOUND, "要删除的资产来源不存在");
        }

        if (deleteAssetSources.get(0).getAssetQuantity() != null
                && deleteAssetSources.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产来源还存在资产");
        }


        assetSourceMapper.deleteById(assetSourceId);
    }

    /**
     * 批量删除资产来源
     *
     * @param assetSourceIds
     */
    @Transactional
    @Override
    public void deleteAssetSourceByBatchId(List<Long> assetSourceIds) throws AssetException {
        List<AssetSource> assetSourceList = assetSourceMapper.selectAssetSourceListByIds(assetSourceIds);

        if (assetSourceList != null && assetSourceList.size() > 0) {
            for (AssetSource assetSource : assetSourceList) {

                if (assetSource.getAssetQuantity() != null && assetSource.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的资产来源存在资产");
                }
            }
        }

        assetSourceMapper.deleteBatchIds(assetSourceIds);
    }

    /**
     * 上传导入资产来源
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAssetSource(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<AssetSource> assetSourceList = new ArrayList<>();
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
                    AssetSource assetSource = new AssetSource();
                    Row row = sheet.getRow(i);

                    Long assetSourceId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(assetSourceId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(assetSourceId);
                    }

                    //数据复值
                    assetSource.setAssetSourceId(assetSourceId);
                    assetSource.setAssetSourceName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    assetSource.setSourceMark(dataFormatter.formatCellValue(row.getCell(2)).trim());
                    assetSource.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(4)).trim()));
                    assetSource.setRemark(dataFormatter.formatCellValue(row.getCell(7)).trim());

                    assetSourceList.add(assetSource);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<AssetSource> assetSources = assetSourceMapper.selectBatchIds(idList);
            if (assetSources.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(assetSourceList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = assetSourceList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, assetSourceList.get(i));
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
