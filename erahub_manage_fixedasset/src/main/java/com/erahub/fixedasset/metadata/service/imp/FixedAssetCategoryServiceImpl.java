package com.erahub.fixedasset.metadata.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.fixedasset.metadata.mapper.FixedAssetCategoryMapper;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/
@Service
@Transactional
public class FixedAssetCategoryServiceImpl extends ServiceImpl<FixedAssetCategoryMapper, FixedAssetCategory> implements FixedAssetCategoryService {

    @Autowired
    private FixedAssetCategoryMapper fixedAssetCategoryMapper;

}
