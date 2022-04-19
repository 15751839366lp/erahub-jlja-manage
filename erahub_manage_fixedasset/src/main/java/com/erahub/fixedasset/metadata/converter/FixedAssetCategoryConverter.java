package com.erahub.fixedasset.metadata.converter;

import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
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
public class FixedAssetCategoryConverter {

    /**
     * 转vo
     * @param fixedAssetCategorys
     * @return
     */
    public static List<FixedAssetCategoryVO> converterToFixedAssetCategoryVOList(List<FixedAssetCategory> fixedAssetCategorys) {
        List<FixedAssetCategoryVO> fixedAssetCategoryVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(fixedAssetCategorys)){
            for (FixedAssetCategory fixedAssetCategory : fixedAssetCategorys) {
                FixedAssetCategoryVO fixedAssetCategoryVO = new FixedAssetCategoryVO();
                BeanUtils.copyProperties(fixedAssetCategory,fixedAssetCategoryVO);

                fixedAssetCategoryVO.setDepreciationPeriod(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getDepreciationPeriod(),100,2)));
                fixedAssetCategoryVO.setEstimatedTotalWorkload(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getEstimatedTotalWorkload(),100,2)));
                fixedAssetCategoryVO.setNetResidualValue(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getNetResidualValue(),100,2)));
                fixedAssetCategoryVO.setStatus(fixedAssetCategory.getStatus() == 1 ? true : false);

                fixedAssetCategoryVOS.add(fixedAssetCategoryVO);
            }
        }
        return fixedAssetCategoryVOS;
    }
}
