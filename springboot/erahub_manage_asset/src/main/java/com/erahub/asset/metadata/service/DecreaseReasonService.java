package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.DecreaseReasonDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseReasonExcel;
import com.erahub.common.model.asset.metadata.DecreaseReason;
import com.erahub.common.vo.asset.metadata.DecreaseReasonVO;
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
public interface DecreaseReasonService extends IService<DecreaseReason> {

    /**
     * 查询减少原因列表
     * @param decreaseReasonDTO
     * @return
     */
    PageVO<DecreaseReasonVO> getDecreaseReasonList(DecreaseReasonDTO decreaseReasonDTO);

    /**
     * 更新减少原因状态
     * @param decreaseReasonId
     * @param status
     */
    void changeDecreaseReasonStatus(Long decreaseReasonId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<DecreaseReasonExcel> exportDecreaseReasonExcel();

    /**
     * 添加减少原因
     * @param decreaseReasonDTO
     */
    void addDecreaseReason(DecreaseReasonDTO decreaseReasonDTO) throws AssetException;

    /**
     * 修改减少原因
     * @param decreaseReasonDTO
     */
    void updateDecreaseReason(DecreaseReasonDTO decreaseReasonDTO) throws AssetException;

    /**
     * 删除减少原因
     * @param decreaseReasonId
     */
    void deleteDecreaseReasonById(Long decreaseReasonId) throws AssetException;

    /**
     * 批量删除减少原因
     * @param decreaseReasonIds
     */
    void deleteDecreaseReasonByBatchId(List<Long> decreaseReasonIds) throws AssetException;

    /**
     * 上传导入减少原因
     * @param fileMap
     * @return
     */
    void importDecreaseReason(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
