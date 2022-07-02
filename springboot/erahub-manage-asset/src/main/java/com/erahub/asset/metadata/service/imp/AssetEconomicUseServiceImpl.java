package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.AssetEconomicUseConverter;
import com.erahub.asset.metadata.mapper.AssetEconomicUseMapper;
import com.erahub.asset.metadata.mapper.AssetEconomicUseMapper;
import com.erahub.asset.metadata.service.AssetEconomicUseService;
import com.erahub.common.dto.asset.metadata.AssetEconomicUseDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetEconomicUseExcel;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.AssetEconomicUseVO;
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
 * @since 2022-05-09
 */
@Service
public class AssetEconomicUseServiceImpl extends ServiceImpl<AssetEconomicUseMapper, AssetEconomicUse> implements AssetEconomicUseService {

    private int batchSize = 1000;

    @Autowired
    private AssetEconomicUseMapper assetEconomicUseMapper;

    @Autowired
    private AssetEconomicUseConverter assetEconomicUseConverter;

    /**
     * 查询经济用途列表
     * @param assetEconomicUseDTO
     * @return
     */
    @Override
    public PageVO<AssetEconomicUseVO> getAssetEconomicUseList(AssetEconomicUseDTO assetEconomicUseDTO) {

        IPage<AssetEconomicUse> assetEconomicUseIPage = new Page<>(assetEconomicUseDTO.getPageNum(), assetEconomicUseDTO.getPageSize());

        assetEconomicUseIPage = assetEconomicUseMapper.selectAssetEconomicUsePageList(assetEconomicUseIPage, assetEconomicUseDTO);
        List<AssetEconomicUse> assetEconomicUseList = assetEconomicUseIPage.getRecords();
        List<AssetEconomicUseVO> assetEconomicUseVOS = assetEconomicUseConverter.converterToAssetEconomicUseVOList(assetEconomicUseList);

        return new PageVO<>(assetEconomicUseIPage.getTotal(), assetEconomicUseVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<AssetEconomicUseExcel> exportAssetEconomicUseExcel() {
        List<AssetEconomicUseExcel> assetEconomicUseExcels = new ArrayList<>();

        IPage<AssetEconomicUse> assetEconomicUseIPage = new Page<>();
        //临时不分页
        assetEconomicUseIPage.setSize(-1l);

        assetEconomicUseIPage = assetEconomicUseMapper.selectAssetEconomicUsePageList(assetEconomicUseIPage, new AssetEconomicUseDTO());
        List<AssetEconomicUse> assetEconomicUseList = assetEconomicUseIPage.getRecords();
        List<AssetEconomicUseVO> assetEconomicUseVOS = assetEconomicUseConverter.converterToAssetEconomicUseVOList(assetEconomicUseList);
        ListMapUtils.copyList(assetEconomicUseVOS, assetEconomicUseExcels, AssetEconomicUseExcel.class);

        return assetEconomicUseExcels;
    }

    /**
     * 更新经济用途状态
     *
     * @param assetEconomicUseId
     * @param status
     */
    @Override
    public void changeAssetEconomicUseStatus(Long assetEconomicUseId, Integer status) throws AssetException {
        AssetEconomicUse dbAssetEconomicUse = assetEconomicUseMapper.selectById(assetEconomicUseId);
        if (dbAssetEconomicUse == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的经济用途不存在");
        }

        dbAssetEconomicUse.setStatus(status);
        assetEconomicUseMapper.updateById(dbAssetEconomicUse);

    }

    /**
     * 添加经济用途
     *
     * @param assetEconomicUseDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addAssetEconomicUse(AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException {
        @NotNull(message = "经济用途id不能为空") Long assetEconomicUseId = assetEconomicUseDTO.getAssetEconomicUseId();
        @NotBlank(message = "经济用途名称不能为空") String assetEconomicUseName = assetEconomicUseDTO.getAssetEconomicUseName();

        QueryWrapper<AssetEconomicUse> assetEconomicUseQueryWrapper = new QueryWrapper<>();
        assetEconomicUseQueryWrapper.eq("asset_economic_use_id", assetEconomicUseId)
                .or()
                .eq("asset_economic_use_name", assetEconomicUseName);

        List<AssetEconomicUse> assetEconomicUseList = assetEconomicUseMapper.selectList(assetEconomicUseQueryWrapper);

        if (!assetEconomicUseList.isEmpty() && assetEconomicUseList.get(0).getAssetEconomicUseId().equals(assetEconomicUseId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该经济用途id已被占用");
        }
        if (!assetEconomicUseList.isEmpty() && assetEconomicUseList.get(0).getAssetEconomicUseName().equals(assetEconomicUseName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该经济用途名称已被占用");
        }

        AssetEconomicUse assetEconomicUse = new AssetEconomicUse();
        assetEconomicUseConverter.converterToAssetEconomicUse(assetEconomicUseDTO, assetEconomicUse);

        assetEconomicUseMapper.insert(assetEconomicUse);
    }

    /**
     * 修改经济用途
     *
     * @param assetEconomicUseDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateAssetEconomicUse(AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException {
        @NotNull(message = "经济用途id不能为空") Long assetEconomicUseId = assetEconomicUseDTO.getAssetEconomicUseId();

        List<AssetEconomicUse> updateAssetEconomicUseList = assetEconomicUseMapper
                .selectList(new QueryWrapper<AssetEconomicUse>().like("asset_economic_use_id", assetEconomicUseId));

        if (updateAssetEconomicUseList == null || updateAssetEconomicUseList.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_ECONOMIC_USE_NOT_FOUND);
        }

        AssetEconomicUse updateAssetEconomicUse = updateAssetEconomicUseList.get(0);
        assetEconomicUseConverter.converterToAssetEconomicUse(assetEconomicUseDTO, updateAssetEconomicUse);

        assetEconomicUseMapper.updateById(updateAssetEconomicUse);
    }

    /**
     * 删除经济用途
     *
     * @param assetEconomicUseId
     */
    @Transactional
    @Override
    public void deleteAssetEconomicUseById(Long assetEconomicUseId) throws AssetException {
        List<AssetEconomicUse> deleteAssetEconomicUses = assetEconomicUseMapper.selectAssetEconomicUseListByIds(Arrays.asList(assetEconomicUseId));

        if (deleteAssetEconomicUses == null || deleteAssetEconomicUses.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_ECONOMIC_USE_NOT_FOUND, "要删除的经济用途不存在");
        }

        if (deleteAssetEconomicUses.get(0).getAssetQuantity() != null
                && deleteAssetEconomicUses.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的经济用途还存在资产");
        }


        assetEconomicUseMapper.deleteById(assetEconomicUseId);
    }

    /**
     * 批量删除经济用途
     *
     * @param assetEconomicUseIds
     */
    @Transactional
    @Override
    public void deleteAssetEconomicUseByBatchId(List<Long> assetEconomicUseIds) throws AssetException {
        List<AssetEconomicUse> assetEconomicUseList = assetEconomicUseMapper.selectAssetEconomicUseListByIds(assetEconomicUseIds);

        if (assetEconomicUseList != null && assetEconomicUseList.size() > 0) {
            for (AssetEconomicUse assetEconomicUse : assetEconomicUseList) {

                if (assetEconomicUse.getAssetQuantity() != null && assetEconomicUse.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的经济用途存在资产");
                }
            }
        }

        assetEconomicUseMapper.deleteBatchIds(assetEconomicUseIds);
    }

    /**
     * 上传导入经济用途
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAssetEconomicUse(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<AssetEconomicUse> assetEconomicUseList = new ArrayList<>();
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
                    AssetEconomicUse assetEconomicUse = new AssetEconomicUse();
                    Row row = sheet.getRow(i);

                    Long assetEconomicUseId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(assetEconomicUseId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(assetEconomicUseId);
                    }

                    //数据复值
                    assetEconomicUse.setAssetEconomicUseId(assetEconomicUseId);
                    assetEconomicUse.setAssetEconomicUseName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    assetEconomicUse.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    assetEconomicUse.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    assetEconomicUseList.add(assetEconomicUse);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<AssetEconomicUse> assetEconomicUses = assetEconomicUseMapper.selectBatchIds(idList);
            if (assetEconomicUses.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(assetEconomicUseList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = assetEconomicUseList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, assetEconomicUseList.get(i));
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
