package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.model.fixedasset.metadata.Section;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import com.erahub.fixedasset.metadata.mapper.SectionMapper;
import com.erahub.fixedasset.metadata.service.SectionService;
import com.erahub.fixedasset.metadata.utils.SectionTreeBuilder;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:10
 * @Version 1.0
 **/
@Service
@Transactional
public class SectionServiceImpl extends ServiceImpl<SectionMapper, Section> implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    /**
     * 获取单位列表
     * @return
     */
    @Override
    public PageVO<SectionVO> getSectionList() {
        IPage<Section> sectionVOIPage = new Page<>(0,-1);

        sectionVOIPage = sectionMapper.selectPage(sectionVOIPage, null);
        List<SectionVO> tree = SectionTreeBuilder.build(sectionVOIPage.getRecords());

        return new PageVO<>(sectionVOIPage.getTotal(),tree);
    }
}
