package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.AssetSourceDTO;
import com.erahub.common.model.asset.metadata.AssetSource;
import com.erahub.common.vo.asset.metadata.AssetSourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/8 9:34
 * @Version 1.0
 **/
@Component
public class AssetSourceConverter {

    /**
     * pojo转vo list
     *
     * @param assetSources
     * @return
     */
    public static List<AssetSourceVO> converterToAssetSourceVOList(List<AssetSource> assetSources) {
        List<AssetSourceVO> assetSourceVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetSources)) {
            for (AssetSource assetSource : assetSources) {
                AssetSourceVO assetSourceVO = new AssetSourceVO();
                BeanUtils.copyProperties(assetSource, assetSourceVO);

                if(assetSource.getAssetQuantity() == null){
                    assetSourceVO.setAssetQuantity(0l);
                }

                assetSourceVOS.add(assetSourceVO);
            }
        }
        return assetSourceVOS;
    }

    /**
     * dto转pojo
     *
     * @param assetSourceDTO
     * @param assetSource
     * @return
     */
    public static void converterToAssetSource(AssetSourceDTO assetSourceDTO, AssetSource assetSource) {
        BeanUtils.copyProperties(assetSourceDTO, assetSource);
    }

    /**
     * dto转pojo list
     *
     * @param assetSourceDTOS
     * @return
     */
    public static List<AssetSource> converterToAssetSourceList(List<AssetSourceDTO> assetSourceDTOS) {
        List<AssetSource> assetSources = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetSourceDTOS)) {
            for (AssetSourceDTO assetSourceDTO : assetSourceDTOS) {
                AssetSource assetSource = new AssetSource();
                BeanUtils.copyProperties(assetSourceDTO, assetSource);

                assetSources.add(assetSource);
            }
        }
        return assetSources;
    }
}
