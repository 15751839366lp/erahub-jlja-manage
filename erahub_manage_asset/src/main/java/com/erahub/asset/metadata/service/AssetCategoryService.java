package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetCategoryExcel;
import com.erahub.common.model.asset.metadata.AssetCategory;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/

public interface AssetCategoryService extends IService<AssetCategory> {

    /**
     * 查询资产类别列表
     * @return
     */
    PageVO<AssetCategoryVO> getAssetCategoryList(AssetCategoryDTO assetCategoryDTO);

    /**
     * 更新资产类别状态
     * @param assetCategoryId
     * @param status
     */
    void changeAssetCategoryStatus(String assetCategoryId, Long status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<AssetCategoryExcel> exportAssetCategoryExcel();

    /**
     * 添加资产类别
     * @param assetCategoryDTO
     */
    void addAssetCategory(AssetCategoryDTO assetCategoryDTO) throws AssetException;

    /**
     * 修改资产类别
     * @param assetCategoryDTO
     */
    void updateAssetCategory(AssetCategoryDTO assetCategoryDTO) throws AssetException;

    /**
     * 删除资产类别
     * @param assetCategoryId
     */
    void deleteAssetCategoryById(String assetCategoryId) throws AssetException;

    /**
     * 批量删除资产类别
     * @param assetCategoryIds
     */
    void deleteAssetCategoryByBatchId(List<String> assetCategoryIds) throws AssetException;

    /**
     * 上传导入资产类别
     * @param fileMap
     * @return
     */
    void importAssetCategory(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
