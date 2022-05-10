package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.AssetOwnershipDTO;
import com.erahub.common.model.asset.metadata.AssetOwnership;
import com.erahub.common.vo.asset.metadata.AssetOwnershipVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/10 17:42
 * @Version 1.0
 **/
@Component
public class AssetOwnershipConverter {
    /**
     * pojo转vo list
     *
     * @param assetOwnerships
     * @return
     */
    public static List<AssetOwnershipVO> converterToAssetOwnershipVOList(List<AssetOwnership> assetOwnerships) {
        List<AssetOwnershipVO> assetOwnershipVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetOwnerships)) {
            for (AssetOwnership assetOwnership : assetOwnerships) {
                AssetOwnershipVO assetOwnershipVO = new AssetOwnershipVO();
                BeanUtils.copyProperties(assetOwnership, assetOwnershipVO);

                if(assetOwnership.getAssetQuantity() == null){
                    assetOwnershipVO.setAssetQuantity(0l);
                }

                assetOwnershipVOS.add(assetOwnershipVO);
            }
        }
        return assetOwnershipVOS;
    }

    /**
     * dto转pojo
     *
     * @param assetOwnershipDTO
     * @param assetOwnership
     * @return
     */
    public static void converterToAssetOwnership(AssetOwnershipDTO assetOwnershipDTO, AssetOwnership assetOwnership) {
        BeanUtils.copyProperties(assetOwnershipDTO, assetOwnership);
    }

    /**
     * dto转pojo list
     *
     * @param assetOwnershipDTOS
     * @return
     */
    public static List<AssetOwnership> converterToAssetOwnershipList(List<AssetOwnershipDTO> assetOwnershipDTOS) {
        List<AssetOwnership> assetOwnerships = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetOwnershipDTOS)) {
            for (AssetOwnershipDTO assetOwnershipDTO : assetOwnershipDTOS) {
                AssetOwnership assetOwnership = new AssetOwnership();
                BeanUtils.copyProperties(assetOwnershipDTO, assetOwnership);

                assetOwnerships.add(assetOwnership);
            }
        }
        return assetOwnerships;
    }
}
