package com.erahub.fixedasset.assetoperation.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.model.fixedasset.assetoperation.FixedAsset;
import com.erahub.fixedasset.assetoperation.mapper.FixedAssetMapper;
import com.erahub.fixedasset.assetoperation.service.FixedAssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:47
 * @Version 1.0
 **/
@Service
@Transactional
public class FixedAssetServiceImpl extends ServiceImpl<FixedAssetMapper, FixedAsset> implements FixedAssetService {
}
