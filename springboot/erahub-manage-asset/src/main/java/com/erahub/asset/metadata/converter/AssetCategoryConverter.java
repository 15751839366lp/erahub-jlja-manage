package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;
import com.erahub.common.model.asset.metadata.AssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/17 11:16
 * @Version 1.0
 **/
@Component
public class AssetCategoryConverter {

    /**
     * pojo转vo list
     *
     * @param assetCategories
     * @return
     */
    public static List<AssetCategoryVO> converterToAssetCategoryVOList(List<AssetCategory> assetCategories) {
        List<AssetCategoryVO> assetCategoryVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetCategories)) {
            for (AssetCategory assetCategory : assetCategories) {
                AssetCategoryVO assetCategoryVO = new AssetCategoryVO();
                BeanUtils.copyProperties(assetCategory, assetCategoryVO);

                if(assetCategory.getAssetQuantity() == null){
                    assetCategoryVO.setAssetQuantity(0l);
                }
                assetCategoryVOS.add(assetCategoryVO);
            }
        }
        return assetCategoryVOS;
    }

    /**
     * dto转pojo
     *
     * @param assetCategoryDTO
     * @return
     */
    public static void converterToAssetCategory(AssetCategoryDTO assetCategoryDTO, AssetCategory assetCategory) {

        BeanUtils.copyProperties(assetCategoryDTO, assetCategory);

        assetCategory.setLevel(Integer.valueOf(assetCategoryDTO.getAssetCategoryId().length() / 2));
    }

    /**
     * dto转pojo list
     *
     * @param assetCategoryDTOS
     * @return
     */
    public static List<AssetCategory> converterToAssetCategoryList(List<AssetCategoryDTO> assetCategoryDTOS) {
        List<AssetCategory> assetCategories = new ArrayList<>();
        if (!CollectionUtils.isEmpty(assetCategoryDTOS)) {
            for (AssetCategoryDTO assetCategoryDTO : assetCategoryDTOS) {
                AssetCategory assetCategory = new AssetCategory();
                BeanUtils.copyProperties(assetCategoryDTO, assetCategory);

                assetCategory.setLevel(Integer.valueOf(assetCategoryDTO.getAssetCategoryId().length() / 2));

                assetCategories.add(assetCategory);
            }
        }
        return assetCategories;
    }
}
