package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.SectionExcel;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;

import java.util.List;

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
    void changeSectionStatus(String sectionId, Long status) throws FixedAssetException;
    /**
     * 添加使用单位
     * @param sectionDTO
     * @return
     */
    void addSection(SectionDTO sectionDTO) throws FixedAssetException;
    /**
     * 获取子节点
     * @param sectionId
     * @return
     */
    PageVO<SectionVO> getChildrenList(String sectionId) throws FixedAssetException;
}
