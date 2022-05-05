package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.DepreciationMethodDTO;
import com.erahub.common.model.asset.metadata.DepreciationMethod;
import com.erahub.common.vo.asset.metadata.DepreciationMethodVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
                if(depreciationMethod.getAssetQuantity() == null){
                    depreciationMethodVO.setAssetQuantity(0l);
                }
                if(depreciationMethod.getAssetCategoryQuantity() == null){
                    depreciationMethodVO.setAssetCategoryQuantity(0l);
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
