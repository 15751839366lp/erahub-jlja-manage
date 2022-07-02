package com.erahub.asset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.SectionExcel;
import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.asset.metadata.SectionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:10
 * @Version 1.0
 **/
public interface SectionService extends IService<Section> {
    /**
     * 获取单位列表
     * @param sectionDTO
     * @return
     */
    PageVO<SectionVO> getSectionList(SectionDTO sectionDTO);
    /**
     * 导出excel
     * @return
     */
    List<SectionExcel> exportSectionExcel();
    /**
     * 更新使用单位状态
     * @param sectionId
     * @param status
     * @return
     */
    void changeSectionStatus(String sectionId, Integer status) throws AssetException;
    /**
     * 添加使用单位
     * @param sectionDTO
     * @return
     */
    void addSection(SectionDTO sectionDTO) throws AssetException;
    /**
     * 获取子节点
     * @param sectionId
     * @return
     */
    PageVO<SectionVO> getChildrenList(String sectionId) throws AssetException;
    /**
     * 修改使用单位
     * @param sectionDTO
     */
    void updateSection(SectionDTO sectionDTO) throws AssetException;
    /**
     * 删除使用单位
     * @param sectionId
     */
    void deleteSectionById(String sectionId) throws AssetException;

    /**
     * 批量删除使用单位
     * @param sectionIds
     */
    void deleteSectionByBatchId(List<String> sectionIds) throws AssetException;

    /**
     * 上传导入使用单位
     * @param fileMap
     * @return
     */
    void importSection(Map<String, MultipartFile> fileMap) throws AssetException, IOException;
}
