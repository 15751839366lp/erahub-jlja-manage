package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
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

public interface FixedAssetCategoryService extends IService<FixedAssetCategory> {

    /**
     * 查询资产类别列表
     * @return
     */
    PageVO<FixedAssetCategoryVO> getFixedAssetCategoryList(FixedAssetCategoryDTO fixedAssetCategoryDTO);

    /**
     * 更新资产类别状态
     * @param categoryId
     * @param status
     */
    void changeFixedAssetCategoryStatus(String categoryId, Long status) throws FixedAssetException;

    /**
     * 导出excel
     * @return
     */
    List<FixedAssetCategoryExcel> exportFixedAssetCategoryExcel();

    /**
     * 添加资产类别
     * @param fixedAssetCategoryDTO
     */
    void addFixedAssetCategory(FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException;

    /**
     * 修改资产类别
     * @param fixedAssetCategoryDTO
     */
    void updateFixedAssetCategory(FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException;

    /**
     * 删除资产类别
     * @param id
     */
    void deleteFixedAssetCategoryById(String id) throws FixedAssetException ;

    /**
     * 批量删除资产类别
     * @param categoryIds
     */
    void deleteFixedAssetCategoryByBatchId(List<String> categoryIds) throws FixedAssetException;

    /**
     * 上传导入资产类别
     * @param fileMap
     * @return
     */
    void importFixedAssetCategory(Map<String, MultipartFile> fileMap) throws FixedAssetException, IOException;
}
