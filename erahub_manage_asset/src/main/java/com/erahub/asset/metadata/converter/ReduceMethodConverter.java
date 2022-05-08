package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.ReduceMethodDTO;
import com.erahub.common.model.asset.metadata.ReduceMethod;
import com.erahub.common.vo.asset.metadata.ReduceMethodVO;
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
public class ReduceMethodConverter {

    /**
     * pojo转vo list
     *
     * @param reduceMethods
     * @return
     */
    public static List<ReduceMethodVO> converterToReduceMethodVOList(List<ReduceMethod> reduceMethods) {
        List<ReduceMethodVO> reduceMethodVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reduceMethods)) {
            for (ReduceMethod reduceMethod : reduceMethods) {
                ReduceMethodVO reduceMethodVO = new ReduceMethodVO();
                BeanUtils.copyProperties(reduceMethod, reduceMethodVO);

                if(reduceMethod.getAssetQuantity() == null){
                    reduceMethodVO.setAssetQuantity(0l);
                }
//                if(section.getSectionDetailed() != null){
//                    sectionVO.setHasChildren(section.getSectionDetailed() == 1 ? false : true);
//                }

                reduceMethodVOS.add(reduceMethodVO);
            }
        }
        return reduceMethodVOS;
    }

    /**
     * dto转pojo
     *
     * @param reduceMethodDTO
     * @param reduceMethod
     * @return
     */
    public static void converterToReduceMethod(ReduceMethodDTO reduceMethodDTO, ReduceMethod reduceMethod) {
        BeanUtils.copyProperties(reduceMethodDTO, reduceMethod);
    }

    /**
     * dto转pojo list
     *
     * @param reduceMethodDTOS
     * @return
     */
    public static List<ReduceMethod> converterToReduceMethodList(List<ReduceMethodDTO> reduceMethodDTOS) {
        List<ReduceMethod> reduceMethods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reduceMethodDTOS)) {
            for (ReduceMethodDTO reduceMethodDTO : reduceMethodDTOS) {
                ReduceMethod reduceMethod = new ReduceMethod();
                BeanUtils.copyProperties(reduceMethodDTO, reduceMethod);

                reduceMethods.add(reduceMethod);
            }
        }
        return reduceMethods;
    }
}
