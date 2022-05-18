package com.erahub.asset.metadata.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.metadata.converter.AssetOwnershipConverter;
import com.erahub.asset.metadata.mapper.AssetOwnershipMapper;
import com.erahub.asset.metadata.mapper.AssetOwnershipMapper;
import com.erahub.asset.metadata.service.AssetOwnershipService;
import com.erahub.common.dto.asset.metadata.AssetOwnershipDTO;
import com.erahub.common.error.asset.AssetCodeEnum;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetOwnershipExcel;
import com.erahub.common.model.asset.metadata.AssetOwnership;
import com.erahub.common.model.asset.metadata.AssetOwnership;
import com.erahub.common.utils.ListMapUtils;
import com.erahub.common.vo.asset.metadata.AssetOwnershipVO;
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
public class AssetOwnershipServiceImpl extends ServiceImpl<AssetOwnershipMapper, AssetOwnership> implements AssetOwnershipService {

    private int batchSize = 1000;

    @Autowired
    private AssetOwnershipMapper assetOwnershipMapper;

    @Autowired
    private AssetOwnershipConverter assetOwnershipConverter;

    /**
     * 查询产权归属列表
     * @param assetOwnershipDTO
     * @return
     */
    @Override
    public PageVO<AssetOwnershipVO> getAssetOwnershipList(AssetOwnershipDTO assetOwnershipDTO) {

        IPage<AssetOwnership> assetOwnershipIPage = new Page<>(assetOwnershipDTO.getPageNum(), assetOwnershipDTO.getPageSize());

        assetOwnershipIPage = assetOwnershipMapper.selectAssetOwnershipPageList(assetOwnershipIPage, assetOwnershipDTO);
        List<AssetOwnership> assetOwnershipList = assetOwnershipIPage.getRecords();
        List<AssetOwnershipVO> assetOwnershipVOS = assetOwnershipConverter.converterToAssetOwnershipVOList(assetOwnershipList);

        return new PageVO<>(assetOwnershipIPage.getTotal(), assetOwnershipVOS);
    }

    /**
     * 导出excel
     *
     * @return
     */
    @Override
    public List<AssetOwnershipExcel> exportAssetOwnershipExcel() {
        List<AssetOwnershipExcel> assetOwnershipExcels = new ArrayList<>();

        IPage<AssetOwnership> assetOwnershipIPage = new Page<>();
        //临时不分页
        assetOwnershipIPage.setSize(-1l);

        assetOwnershipIPage = assetOwnershipMapper.selectAssetOwnershipPageList(assetOwnershipIPage, new AssetOwnershipDTO());
        List<AssetOwnership> assetOwnershipList = assetOwnershipIPage.getRecords();
        List<AssetOwnershipVO> assetOwnershipVOS = assetOwnershipConverter.converterToAssetOwnershipVOList(assetOwnershipList);
        ListMapUtils.copyList(assetOwnershipVOS, assetOwnershipExcels, AssetOwnershipExcel.class);

        return assetOwnershipExcels;
    }

    /**
     * 更新产权归属状态
     *
     * @param assetOwnershipId
     * @param status
     */
    @Override
    public void changeAssetOwnershipStatus(Long assetOwnershipId, Integer status) throws AssetException {
        AssetOwnership dbAssetOwnership = assetOwnershipMapper.selectById(assetOwnershipId);
        if (dbAssetOwnership == null) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要更新状态的产权归属不存在");
        }

        dbAssetOwnership.setStatus(status);
        assetOwnershipMapper.updateById(dbAssetOwnership);

    }

    /**
     * 添加产权归属
     *
     * @param assetOwnershipDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void addAssetOwnership(AssetOwnershipDTO assetOwnershipDTO) throws AssetException {
        @NotNull(message = "产权归属id不能为空") Long assetOwnershipId = assetOwnershipDTO.getAssetOwnershipId();
        @NotBlank(message = "产权归属名称不能为空") String assetOwnershipName = assetOwnershipDTO.getAssetOwnershipName();

        QueryWrapper<AssetOwnership> assetOwnershipQueryWrapper = new QueryWrapper<>();
        assetOwnershipQueryWrapper.eq("asset_ownership_id", assetOwnershipId)
                .or()
                .eq("asset_ownership_name", assetOwnershipName);

        List<AssetOwnership> assetOwnershipList = assetOwnershipMapper.selectList(assetOwnershipQueryWrapper);

        if (!assetOwnershipList.isEmpty() && assetOwnershipList.get(0).getAssetOwnershipId().equals(assetOwnershipId)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该产权归属id已被占用");
        }
        if (!assetOwnershipList.isEmpty() && assetOwnershipList.get(0).getAssetOwnershipName().equals(assetOwnershipName)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "该产权归属名称已被占用");
        }

        AssetOwnership assetOwnership = new AssetOwnership();
        assetOwnershipConverter.converterToAssetOwnership(assetOwnershipDTO, assetOwnership);

        assetOwnershipMapper.insert(assetOwnership);
    }

    /**
     * 修改产权归属
     *
     * @param assetOwnershipDTO
     * @throws AssetException
     */
    @Transactional
    @Override
    public void updateAssetOwnership(AssetOwnershipDTO assetOwnershipDTO) throws AssetException {
        @NotNull(message = "产权归属id不能为空") Long assetOwnershipId = assetOwnershipDTO.getAssetOwnershipId();

        List<AssetOwnership> updateAssetOwnershipList = assetOwnershipMapper
                .selectList(new QueryWrapper<AssetOwnership>().like("asset_ownership_id", assetOwnershipId));

        if (updateAssetOwnershipList == null || updateAssetOwnershipList.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_OWNERSHIP_NOT_FOUND);
        }

        AssetOwnership updateAssetOwnership = updateAssetOwnershipList.get(0);
        assetOwnershipConverter.converterToAssetOwnership(assetOwnershipDTO, updateAssetOwnership);

        assetOwnershipMapper.updateById(updateAssetOwnership);
    }

    /**
     * 删除产权归属
     *
     * @param assetOwnershipId
     */
    @Transactional
    @Override
    public void deleteAssetOwnershipById(Long assetOwnershipId) throws AssetException {
        List<AssetOwnership> deleteAssetOwnerships = assetOwnershipMapper.selectAssetOwnershipListByIds(Arrays.asList(assetOwnershipId));

        if (deleteAssetOwnerships == null || deleteAssetOwnerships.size() == 0) {
            throw new AssetException(AssetCodeEnum.ASSET_OWNERSHIP_NOT_FOUND, "要删除的产权归属不存在");
        }

        if (deleteAssetOwnerships.get(0).getAssetQuantity() != null
                && deleteAssetOwnerships.get(0).getAssetQuantity() > 0) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的产权归属还存在资产");
        }


        assetOwnershipMapper.deleteById(assetOwnershipId);
    }

    /**
     * 批量删除产权归属
     *
     * @param assetOwnershipIds
     */
    @Transactional
    @Override
    public void deleteAssetOwnershipByBatchId(List<Long> assetOwnershipIds) throws AssetException {
        List<AssetOwnership> assetOwnershipList = assetOwnershipMapper.selectAssetOwnershipListByIds(assetOwnershipIds);

        if (assetOwnershipList != null && assetOwnershipList.size() > 0) {
            for (AssetOwnership assetOwnership : assetOwnershipList) {

                if (assetOwnership.getAssetQuantity() != null && assetOwnership.getAssetQuantity() > 0) {
                    throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "要删除的产权归属存在资产");
                }
            }
        }

        assetOwnershipMapper.deleteBatchIds(assetOwnershipIds);
    }

    /**
     * 上传导入产权归属
     *
     * @param fileMap
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAssetOwnership(Map<String, MultipartFile> fileMap) throws AssetException, IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        List<AssetOwnership> assetOwnershipList = new ArrayList<>();
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
                    AssetOwnership assetOwnership = new AssetOwnership();
                    Row row = sheet.getRow(i);

                    Long assetOwnershipId = Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)).trim());
                    //判断ID格式

                    //ID和明细存入map
                    if (idList.contains(assetOwnershipId)) {
                        throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
                    } else {
                        idList.add(assetOwnershipId);
                    }

                    //数据复值
                    assetOwnership.setAssetOwnershipId(assetOwnershipId);
                    assetOwnership.setAssetOwnershipName(dataFormatter.formatCellValue(row.getCell(1)).trim());
                    assetOwnership.setStatus(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(3)).trim()));
                    assetOwnership.setRemark(dataFormatter.formatCellValue(row.getCell(6)).trim());

                    assetOwnershipList.add(assetOwnership);
                }
            }

        }catch (Exception e ){
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, e.getMessage());
        }

        //查询表中是否存在重复ID
        if (!idList.isEmpty()) {
            List<AssetOwnership> assetOwnerships = assetOwnershipMapper.selectBatchIds(idList);
            if (assetOwnerships.size() != 0) {
                throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "存在重复ID");
            }
        }

        //批量插入方法
        if (CollectionUtils.isEmpty(assetOwnershipList)) {
            throw new AssetException(AssetCodeEnum.PARAMETER_ERROR, "表格内容为空");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = assetOwnershipList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, assetOwnershipList.get(i));
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
