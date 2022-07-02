package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.AssetSourceDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetSourceExcel;
import com.erahub.common.model.asset.metadata.AssetSource;
import com.erahub.common.vo.asset.metadata.AssetSourceVO;
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
 * @since 2022-05-08
 */
public interface AssetSourceService extends IService<AssetSource> {

    /**
     * 查询资产来源列表
     * @param assetSourceDTO
     * @return
     */
    PageVO<AssetSourceVO> getAssetSourceList(AssetSourceDTO assetSourceDTO);

    /**
     * 更新资产来源状态
     * @param assetSourceId
     * @param status
     */
    void changeAssetSourceStatus(Long assetSourceId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<AssetSourceExcel> exportAssetSourceExcel();

    /**
     * 添加资产来源
     * @param assetSourceDTO
     */
    void addAssetSource(AssetSourceDTO assetSourceDTO) throws AssetException;

    /**
     * 修改资产来源
     * @param assetSourceDTO
     */
    void updateAssetSource(AssetSourceDTO assetSourceDTO) throws AssetException;

    /**
     * 删除资产来源
     * @param assetSourceId
     */
    void deleteAssetSourceById(Long assetSourceId) throws AssetException;

    /**
     * 批量删除资产来源
     * @param assetSourceIds
     */
    void deleteAssetSourceByBatchId(List<Long> assetSourceIds) throws AssetException;

    /**
     * 上传导入资产来源
     * @param fileMap
     * @return
     */
    void importAssetSource(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
