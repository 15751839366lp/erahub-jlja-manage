package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.DecreaseMethodDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseMethodExcel;
import com.erahub.common.model.asset.metadata.DecreaseMethod;
import com.erahub.common.vo.asset.metadata.DecreaseMethodVO;
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
public interface DecreaseMethodService extends IService<DecreaseMethod> {

    /**
     * 查询减少方式列表
     * @param decreaseMethodDTO
     * @return
     */
    PageVO<DecreaseMethodVO> getDecreaseMethodList(DecreaseMethodDTO decreaseMethodDTO);

    /**
     * 更新减少方式状态
     * @param decreaseMethodId
     * @param status
     */
    void changeDecreaseMethodStatus(Long decreaseMethodId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<DecreaseMethodExcel> exportDecreaseMethodExcel();

    /**
     * 添加减少方式
     * @param decreaseMethodDTO
     */
    void addDecreaseMethod(DecreaseMethodDTO decreaseMethodDTO) throws AssetException;

    /**
     * 修改减少方式
     * @param decreaseMethodDTO
     */
    void updateDecreaseMethod(DecreaseMethodDTO decreaseMethodDTO) throws AssetException;

    /**
     * 删除减少方式
     * @param decreaseMethodId
     */
    void deleteDecreaseMethodById(Long decreaseMethodId) throws AssetException;

    /**
     * 批量删除减少方式
     * @param decreaseMethodIds
     */
    void deleteDecreaseMethodByBatchId(List<Long> decreaseMethodIds) throws AssetException;

    /**
     * 上传导入减少方式
     * @param fileMap
     * @return
     */
    void importDecreaseMethod(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
