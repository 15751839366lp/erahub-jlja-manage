package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.AssetEconomicUseDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetEconomicUseExcel;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import com.erahub.common.vo.asset.metadata.AssetEconomicUseVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lipeng
 * @since 2022-05-09
 */
public interface AssetEconomicUseService extends IService<AssetEconomicUse> {

    /**
     * 查询经济用途列表
     * @param assetEconomicUseDTO
     * @return
     */
    PageVO<AssetEconomicUseVO> getAssetEconomicUseList(AssetEconomicUseDTO assetEconomicUseDTO);

    /**
     * 更新经济用途状态
     * @param assetEconomicUseId
     * @param status
     */
    void changeAssetEconomicUseStatus(Long assetEconomicUseId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<AssetEconomicUseExcel> exportAssetEconomicUseExcel();

    /**
     * 添加经济用途
     * @param assetEconomicUseDTO
     */
    void addAssetEconomicUse(AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException;

    /**
     * 修改经济用途
     * @param assetEconomicUseDTO
     */
    void updateAssetEconomicUse(AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException;

    /**
     * 删除经济用途
     * @param assetEconomicUseId
     */
    void deleteAssetEconomicUseById(Long assetEconomicUseId) throws AssetException;

    /**
     * 批量删除经济用途
     * @param assetEconomicUseIds
     */
    void deleteAssetEconomicUseByBatchId(List<Long> assetEconomicUseIds) throws AssetException;

    /**
     * 上传导入经济用途
     * @param fileMap
     * @return
     */
    void importAssetEconomicUse(Map<String, MultipartFile> fileMap) throws AssetException, IOException;

}
