package com.erahub.fixedasset.metadata.converter;

import com.erahub.common.dto.fixedasset.metadata.DepreciationMethodDTO;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.fixedasset.metadata.DepreciationMethodVO;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/27 16:10
 * @Version 1.0
 **/
@Component
public class DepreciationMethodConverter {
    /**
     * pojo转vo list
     *
     * @param depreciationMethods
     * @return
     */
    public static List<DepreciationMethodVO> converterToDepreciationMethodVOList(List<DepreciationMethod> depreciationMethods) {
        List<DepreciationMethodVO> depreciationMethodVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(depreciationMethods)) {
            for (DepreciationMethod depreciationMethod : depreciationMethods) {
                DepreciationMethodVO depreciationMethodVO = new DepreciationMethodVO();
                BeanUtils.copyProperties(depreciationMethod, depreciationMethodVO);
                if(depreciationMethod.getFixedAssetQuantity() == null){
                    depreciationMethodVO.setFixedAssetQuantity(0l);
                }
                if(depreciationMethod.getFixedAssetCategoryQuantity() == null){
                    depreciationMethodVO.setFixedAssetCategoryQuantity(0l);
                }
                depreciationMethodVOS.add(depreciationMethodVO);
            }
        }
        return depreciationMethodVOS;
    }

    /**
     * dto转pojo
     *
     * @param depreciationMethodDTO
     * @return
     */
    public static void converterToDepreciationMethod(DepreciationMethodDTO depreciationMethodDTO,DepreciationMethod depreciationMethod) {
        BeanUtils.copyProperties(depreciationMethodDTO, depreciationMethod);
    }

    /**
     * dto转pojo list
     *
     * @param depreciationMethodDTOS
     * @return
     */
    public static List<DepreciationMethod> converterToDepreciationMethodList(List<DepreciationMethodDTO> depreciationMethodDTOS) {
        List<DepreciationMethod> depreciationMethods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(depreciationMethodDTOS)) {
            for (DepreciationMethodDTO depreciationMethodDTO : depreciationMethodDTOS) {
                DepreciationMethod depreciationMethod = new DepreciationMethod();
                BeanUtils.copyProperties(depreciationMethodDTO, depreciationMethod);

                depreciationMethods.add(depreciationMethod);
            }
        }
        return depreciationMethods;
    }
}
