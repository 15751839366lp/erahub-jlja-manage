package com.erahub.asset.assetdetail.service.imp;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erahub.asset.assetdetail.mapper.AssetMapper;
import com.erahub.asset.assetdetail.service.AssetService;
import com.erahub.common.model.asset.assetdetail.Asset;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lipeng
 * @since 2022-05-13
 */
@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements AssetService {

}
