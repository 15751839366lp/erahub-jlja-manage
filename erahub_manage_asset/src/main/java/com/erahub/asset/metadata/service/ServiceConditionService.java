package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ServiceConditionExcel;
import com.erahub.common.model.asset.metadata.ServiceCondition;
import com.erahub.common.vo.asset.metadata.ServiceConditionVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:51
 * @Version 1.0
 **/
public interface ServiceConditionService extends IService<ServiceCondition> {

    /**
     * 查询使用状态列表
     * @param serviceConditionDTO
     * @return
     */
    PageVO<ServiceConditionVO> getServiceConditionList(ServiceConditionDTO serviceConditionDTO);

    /**
     * 更新使用状态状态
     * @param serviceConditionId
     * @param status
     */
    void changeServiceConditionStatus(Long serviceConditionId, Long status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<ServiceConditionExcel> exportServiceConditionExcel();

    /**
     * 添加使用状态
     * @param serviceConditionDTO
     */
    void addServiceCondition(ServiceConditionDTO serviceConditionDTO) throws AssetException;

    /**
     * 修改使用状态
     * @param serviceConditionDTO
     */
    void updateServiceCondition(ServiceConditionDTO serviceConditionDTO) throws AssetException;

    /**
     * 删除使用状态
     * @param serviceConditionId
     */
    void deleteServiceConditionById(Long serviceConditionId) throws AssetException;

    /**
     * 批量删除使用状态
     * @param serviceConditionIds
     */
    void deleteServiceConditionByBatchId(List<Long> serviceConditionIds) throws AssetException;

    /**
     * 上传导入使用状态
     * @param fileMap
     * @return
     */
    void importServiceCondition(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
