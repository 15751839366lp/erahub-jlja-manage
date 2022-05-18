package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.AssetProjectDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetProjectExcel;
import com.erahub.common.model.asset.metadata.AssetProject;
import com.erahub.common.vo.asset.metadata.AssetProjectVO;
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
public interface AssetProjectService extends IService<AssetProject> {

    /**
     * 查询工程项目列表
     * @param assetProjectDTO
     * @return
     */
    PageVO<AssetProjectVO> getAssetProjectList(AssetProjectDTO assetProjectDTO);

    /**
     * 更新工程项目状态
     * @param assetProjectId
     * @param status
     */
    void changeAssetProjectStatus(String assetProjectId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<AssetProjectExcel> exportAssetProjectExcel();

    /**
     * 添加工程项目
     * @param assetProjectDTO
     */
    void addAssetProject(AssetProjectDTO assetProjectDTO) throws AssetException;

    /**
     * 修改工程项目
     * @param assetProjectDTO
     */
    void updateAssetProject(AssetProjectDTO assetProjectDTO) throws AssetException;

    /**
     * 删除工程项目
     * @param assetProjectId
     */
    void deleteAssetProjectById(String assetProjectId) throws AssetException;

    /**
     * 批量删除工程项目
     * @param assetProjectIds
     */
    void deleteAssetProjectByBatchId(List<String> assetProjectIds) throws AssetException;

    /**
     * 上传导入工程项目
     * @param fileMap
     * @return
     */
    void importAssetProject(Map<String, MultipartFile> fileMap) throws AssetException, IOException;

}
