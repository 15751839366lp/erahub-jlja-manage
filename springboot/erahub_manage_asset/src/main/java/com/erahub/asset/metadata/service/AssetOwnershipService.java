package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.AssetOwnershipDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetOwnershipExcel;
import com.erahub.common.model.asset.metadata.AssetOwnership;
import com.erahub.common.vo.asset.metadata.AssetOwnershipVO;
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
public interface AssetOwnershipService extends IService<AssetOwnership> {

    /**
     * 查询产权归属列表
     * @param assetOwnershipDTO
     * @return
     */
    PageVO<AssetOwnershipVO> getAssetOwnershipList(AssetOwnershipDTO assetOwnershipDTO);

    /**
     * 更新产权归属状态
     * @param assetOwnershipId
     * @param status
     */
    void changeAssetOwnershipStatus(Long assetOwnershipId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<AssetOwnershipExcel> exportAssetOwnershipExcel();

    /**
     * 添加产权归属
     * @param assetOwnershipDTO
     */
    void addAssetOwnership(AssetOwnershipDTO assetOwnershipDTO) throws AssetException;

    /**
     * 修改产权归属
     * @param assetOwnershipDTO
     */
    void updateAssetOwnership(AssetOwnershipDTO assetOwnershipDTO) throws AssetException;

    /**
     * 删除产权归属
     * @param assetOwnershipId
     */
    void deleteAssetOwnershipById(Long assetOwnershipId) throws AssetException;

    /**
     * 批量删除产权归属
     * @param assetOwnershipIds
     */
    void deleteAssetOwnershipByBatchId(List<Long> assetOwnershipIds) throws AssetException;

    /**
     * 上传导入产权归属
     * @param fileMap
     * @return
     */
    void importAssetOwnership(Map<String, MultipartFile> fileMap) throws AssetException, IOException;

}
