package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.AssetEconomicUseDTO;
import com.erahub.common.model.asset.metadata.AssetEconomicUse;
import com.erahub.common.vo.asset.metadata.AssetEconomicUseVO;
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
public class AssetEconomicUseConverter {
    /**
     * pojo转vo list
     *
     * @param assetEconomicUses
     * @return
     */
    public static List<AssetEconomicUseVO> converterToAssetEconomicUseVOList(List<AssetEconomicUse> assetEconomicUses) {
        List<AssetEconomicUseVO> assetEconomicUseVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetEconomicUses)) {
            for (AssetEconomicUse assetEconomicUse : assetEconomicUses) {
                AssetEconomicUseVO assetEconomicUseVO = new AssetEconomicUseVO();
                BeanUtils.copyProperties(assetEconomicUse, assetEconomicUseVO);

                if(assetEconomicUse.getAssetQuantity() == null){
                    assetEconomicUseVO.setAssetQuantity(0l);
                }

                assetEconomicUseVOS.add(assetEconomicUseVO);
            }
        }
        return assetEconomicUseVOS;
    }

    /**
     * dto转pojo
     *
     * @param assetEconomicUseDTO
     * @param assetEconomicUse
     * @return
     */
    public static void converterToAssetEconomicUse(AssetEconomicUseDTO assetEconomicUseDTO, AssetEconomicUse assetEconomicUse) {
        BeanUtils.copyProperties(assetEconomicUseDTO, assetEconomicUse);
    }

    /**
     * dto转pojo list
     *
     * @param assetEconomicUseDTOS
     * @return
     */
    public static List<AssetEconomicUse> converterToAssetEconomicUseList(List<AssetEconomicUseDTO> assetEconomicUseDTOS) {
        List<AssetEconomicUse> assetEconomicUses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetEconomicUseDTOS)) {
            for (AssetEconomicUseDTO assetEconomicUseDTO : assetEconomicUseDTOS) {
                AssetEconomicUse assetEconomicUse = new AssetEconomicUse();
                BeanUtils.copyProperties(assetEconomicUseDTO, assetEconomicUse);

                assetEconomicUses.add(assetEconomicUse);
            }
        }
        return assetEconomicUses;
    }
}
