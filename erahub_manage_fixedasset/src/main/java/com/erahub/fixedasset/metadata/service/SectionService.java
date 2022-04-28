package com.erahub.fixedasset.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:10
 * @Version 1.0
 **/
public interface SectionService extends IService<Section> {
    /**
     * 获取单位列表
     * @return
     */
    PageVO<SectionVO> getSectionList();
}
