package com.erahub.asset.assetoperation.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.common.model.asset.assetdetail.Asset;
import com.erahub.asset.assetoperation.mapper.AssetMapper;
import com.erahub.asset.assetoperation.service.AssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:47
 * @Version 1.0
 **/
@Service
@Transactional
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements AssetService {
}
