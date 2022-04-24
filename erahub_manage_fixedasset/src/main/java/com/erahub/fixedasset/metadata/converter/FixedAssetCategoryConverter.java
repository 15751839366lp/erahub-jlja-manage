package com.erahub.fixedasset.metadata.converter;

import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
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
     * pojo转vo list
     *
     * @param fixedAssetCategorys
     * @return
     */
    public static List<FixedAssetCategoryVO> converterToFixedAssetCategoryVOList(List<FixedAssetCategory> fixedAssetCategorys) {
        List<FixedAssetCategoryVO> fixedAssetCategoryVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fixedAssetCategorys)) {
            for (FixedAssetCategory fixedAssetCategory : fixedAssetCategorys) {
                FixedAssetCategoryVO fixedAssetCategoryVO = new FixedAssetCategoryVO();
                BeanUtils.copyProperties(fixedAssetCategory, fixedAssetCategoryVO);

                fixedAssetCategoryVO.setDepreciationPeriod(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getDepreciationPeriod(), 100, 2)));
                fixedAssetCategoryVO.setEstimatedTotalWorkload(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getEstimatedTotalWorkload(), 100, 2)));
                fixedAssetCategoryVO.setNetResidualValue(new BigDecimal(ArithmeticUtils.div(fixedAssetCategory.getNetResidualValue(), 100, 2)));
                fixedAssetCategoryVO.setStatus(fixedAssetCategory.getStatus() == 1 ? true : false);

                fixedAssetCategoryVOS.add(fixedAssetCategoryVO);
            }
        }
        return fixedAssetCategoryVOS;
    }

    /**
     * dto转pojo
     *
     * @param fixedAssetCategoryDTO
     * @return
     */
    public static FixedAssetCategory converterToFixedAssetCategory(FixedAssetCategoryDTO fixedAssetCategoryDTO) {

        FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
        BeanUtils.copyProperties(fixedAssetCategoryDTO, fixedAssetCategory);

        fixedAssetCategory.setDepreciationPeriod(ArithmeticUtils.mul(fixedAssetCategoryDTO.getDepreciationPeriod().toString(), "100").longValue());
        fixedAssetCategory.setEstimatedTotalWorkload(ArithmeticUtils.mul(fixedAssetCategoryDTO.getEstimatedTotalWorkload().toString(), "100").longValue());
        fixedAssetCategory.setNetResidualValue(ArithmeticUtils.mul(fixedAssetCategoryDTO.getNetResidualValue().toString(), "100").longValue());
        fixedAssetCategory.setStatus(fixedAssetCategoryDTO.getStatus() ? 1l : 0l);
        fixedAssetCategory.setCategoryDetailed(fixedAssetCategoryDTO.getCategoryDetailed() ? 1l : 0l);
        fixedAssetCategory.setCategoryLevel(Long.valueOf(fixedAssetCategoryDTO.getCategoryId().toString().length() / 2 + 1));

        return fixedAssetCategory;
    }

    /**
     * dto转pojo list
     *
     * @param fixedAssetCategoryDTOS
     * @return
     */
    public static List<FixedAssetCategory> converterToFixedAssetCategoryList(List<FixedAssetCategoryDTO> fixedAssetCategoryDTOS) {
        List<FixedAssetCategory> fixedAssetCategorys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fixedAssetCategoryDTOS)) {
            for (FixedAssetCategoryDTO fixedAssetCategoryDTO : fixedAssetCategoryDTOS) {
                FixedAssetCategory fixedAssetCategory = new FixedAssetCategory();
                BeanUtils.copyProperties(fixedAssetCategoryDTO, fixedAssetCategory);

                fixedAssetCategory.setDepreciationPeriod(Long.valueOf(ArithmeticUtils.mul(fixedAssetCategoryDTO.getDepreciationPeriod().toString(), "100", 2)));
                fixedAssetCategory.setEstimatedTotalWorkload(Long.valueOf(ArithmeticUtils.mul(fixedAssetCategoryDTO.getEstimatedTotalWorkload().toString(), "100", 2)));
                fixedAssetCategory.setNetResidualValue(Long.valueOf(ArithmeticUtils.mul(fixedAssetCategoryDTO.getNetResidualValue().toString(), "100", 2)));
                fixedAssetCategory.setStatus(fixedAssetCategoryDTO.getStatus() ? 1l : 0l);
                fixedAssetCategory.setCategoryDetailed(fixedAssetCategoryDTO.getCategoryDetailed() ? 1l : 0l);
                fixedAssetCategory.setCategoryLevel(Long.valueOf(fixedAssetCategoryDTO.getCategoryId().toString().length() / 2 + 1));

                fixedAssetCategorys.add(fixedAssetCategory);
            }
        }
        return fixedAssetCategorys;
    }
}
