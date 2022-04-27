package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.fixedasset.metadata.DepreciationMethodDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.DepreciationMethodExcel;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.DepreciationMethodVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @Author lipeng
 * @Date 2022/4/21 18:19
 * @Version 1.0
 **/
public interface DepreciationMethodService extends IService<DepreciationMethod> {
    /**
     * 查询折旧方法列表
     * @param depreciationMethodDTO
     * @return
     */
    PageVO<DepreciationMethodVO> getDepreciationMethodList(DepreciationMethodDTO depreciationMethodDTO);

    /**
     * 导出excel
     * @return
     */
    List<DepreciationMethodExcel> exportDepreciationMethodExcel();

    /**
     * 更新折旧方法状态
     * @param depreciationMethodId
     * @param status
     */
    void changeDepreciationMethodStatus(Long depreciationMethodId, Long status) throws FixedAssetException;

    /**
     * 添加折旧方法
     * @param depreciationMethodDTO
     */
    void addDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException;

    /**
     * 修改折旧方法
     * @param depreciationMethodDTO
     */
    void updateDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException;

    /**
     * 删除折旧方法
     * @param id
     */
    void deleteDepreciationMethod(Long id) throws FixedAssetException;

    /**
     * 批量删除折旧方法
     * @param depreciationMethodIds
     */
    void deleteDepreciationMethodByBatchId(List<Long> depreciationMethodIds) throws FixedAssetException;

    /**
     * 上传导入折旧方法
     * @param fileMap
     */
    void importDepreciationMethod(Map<String, MultipartFile> fileMap) throws FixedAssetException, IOException;

}
