package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.ReduceReasonDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceReasonExcel;
import com.erahub.common.model.asset.metadata.ReduceReason;
import com.erahub.common.vo.asset.metadata.ReduceReasonVO;
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
public interface ReduceReasonService extends IService<ReduceReason> {

    /**
     * 查询减少原因列表
     * @param reduceReasonDTO
     * @return
     */
    PageVO<ReduceReasonVO> getReduceReasonList(ReduceReasonDTO reduceReasonDTO);

    /**
     * 更新减少原因状态
     * @param reduceReasonId
     * @param status
     */
    void changeReduceReasonStatus(Long reduceReasonId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<ReduceReasonExcel> exportReduceReasonExcel();

    /**
     * 添加减少原因
     * @param reduceReasonDTO
     */
    void addReduceReason(ReduceReasonDTO reduceReasonDTO) throws AssetException;

    /**
     * 修改减少原因
     * @param reduceReasonDTO
     */
    void updateReduceReason(ReduceReasonDTO reduceReasonDTO) throws AssetException;

    /**
     * 删除减少原因
     * @param reduceReasonId
     */
    void deleteReduceReasonById(Long reduceReasonId) throws AssetException;

    /**
     * 批量删除减少原因
     * @param reduceReasonIds
     */
    void deleteReduceReasonByBatchId(List<Long> reduceReasonIds) throws AssetException;

    /**
     * 上传导入减少原因
     * @param fileMap
     * @return
     */
    void importReduceReason(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
