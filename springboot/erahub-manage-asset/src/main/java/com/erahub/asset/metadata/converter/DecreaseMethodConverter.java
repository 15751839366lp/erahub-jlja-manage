package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.DecreaseMethodDTO;
import com.erahub.common.model.asset.metadata.DecreaseMethod;
import com.erahub.common.vo.asset.metadata.DecreaseMethodVO;
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
public class DecreaseMethodConverter {

    /**
     * pojo转vo list
     *
     * @param decreaseMethods
     * @return
     */
    public static List<DecreaseMethodVO> converterToDecreaseMethodVOList(List<DecreaseMethod> decreaseMethods) {
        List<DecreaseMethodVO> decreaseMethodVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(decreaseMethods)) {
            for (DecreaseMethod decreaseMethod : decreaseMethods) {
                DecreaseMethodVO decreaseMethodVO = new DecreaseMethodVO();
                BeanUtils.copyProperties(decreaseMethod, decreaseMethodVO);

                if(decreaseMethod.getAssetQuantity() == null){
                    decreaseMethodVO.setAssetQuantity(0l);
                }

                decreaseMethodVOS.add(decreaseMethodVO);
            }
        }
        return decreaseMethodVOS;
    }

    /**
     * dto转pojo
     *
     * @param decreaseMethodDTO
     * @param decreaseMethod
     * @return
     */
    public static void converterToDecreaseMethod(DecreaseMethodDTO decreaseMethodDTO, DecreaseMethod decreaseMethod) {
        BeanUtils.copyProperties(decreaseMethodDTO, decreaseMethod);
    }

    /**
     * dto转pojo list
     *
     * @param decreaseMethodDTOS
     * @return
     */
    public static List<DecreaseMethod> converterToDecreaseMethodList(List<DecreaseMethodDTO> decreaseMethodDTOS) {
        List<DecreaseMethod> decreaseMethods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(decreaseMethodDTOS)) {
            for (DecreaseMethodDTO decreaseMethodDTO : decreaseMethodDTOS) {
                DecreaseMethod decreaseMethod = new DecreaseMethod();
                BeanUtils.copyProperties(decreaseMethodDTO, decreaseMethod);

                decreaseMethods.add(decreaseMethod);
            }
        }
        return decreaseMethods;
    }
}
