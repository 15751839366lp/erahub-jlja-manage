package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.TechnicalConditionDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.TechnicalConditionExcel;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import com.erahub.common.vo.asset.metadata.TechnicalConditionVO;
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
public interface TechnicalConditionService extends IService<TechnicalCondition> {

    /**
     * 查询技术状态列表
     * @param technicalConditionDTO
     * @return
     */
    PageVO<TechnicalConditionVO> getTechnicalConditionList(TechnicalConditionDTO technicalConditionDTO);

    /**
     * 更新技术状态状态
     * @param technicalConditionId
     * @param status
     */
    void changeTechnicalConditionStatus(Long technicalConditionId, Integer status) throws AssetException;

    /**
     * 导出excel
     * @return
     */
    List<TechnicalConditionExcel> exportTechnicalConditionExcel();

    /**
     * 添加技术状态
     * @param technicalConditionDTO
     */
    void addTechnicalCondition(TechnicalConditionDTO technicalConditionDTO) throws AssetException;

    /**
     * 修改技术状态
     * @param technicalConditionDTO
     */
    void updateTechnicalCondition(TechnicalConditionDTO technicalConditionDTO) throws AssetException;

    /**
     * 删除技术状态
     * @param technicalConditionId
     */
    void deleteTechnicalConditionById(Long technicalConditionId) throws AssetException;

    /**
     * 批量删除技术状态
     * @param technicalConditionIds
     */
    void deleteTechnicalConditionByBatchId(List<Long> technicalConditionIds) throws AssetException;

    /**
     * 上传导入技术状态
     * @param fileMap
     * @return
     */
    void importTechnicalCondition(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
