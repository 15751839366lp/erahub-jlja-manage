package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.SectionDTO;
import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.model.asset.metadata.ServiceCondition;
import com.erahub.common.vo.asset.metadata.SectionVO;
import com.erahub.common.vo.asset.metadata.ServiceConditionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:42
 * @Version 1.0
 **/
@Component
public class ServiceConditionConverter {
    /**
     * pojo转vo list
     *
     * @param serviceConditions
     * @return
     */
    public static List<ServiceConditionVO> converterToServiceConditionVOList(List<ServiceCondition> serviceConditions) {
        List<ServiceConditionVO> serviceConditionVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(serviceConditions)) {
            for (ServiceCondition serviceCondition : serviceConditions) {
                ServiceConditionVO serviceConditionVO = new ServiceConditionVO();
                BeanUtils.copyProperties(serviceCondition, serviceConditionVO);

                if(serviceCondition.getAssetQuantity() == null){
                    serviceConditionVO.setAssetQuantity(0l);
                }
//                if(section.getSectionDetailed() != null){
//                    sectionVO.setHasChildren(section.getSectionDetailed() == 1 ? false : true);
//                }

                serviceConditionVOS.add(serviceConditionVO);
            }
        }
        return serviceConditionVOS;
    }

    /**
     * dto转pojo
     *
     * @param serviceConditionDTO
     * @param serviceCondition
     * @return
     */
    public static void converterToServiceCondition(ServiceConditionDTO serviceConditionDTO, ServiceCondition serviceCondition) {
        BeanUtils.copyProperties(serviceConditionDTO, serviceCondition);
    }

    /**
     * dto转pojo list
     *
     * @param serviceConditionDTOS
     * @return
     */
    public static List<ServiceCondition> converterToServiceConditionList(List<ServiceConditionDTO> serviceConditionDTOS) {
        List<ServiceCondition> serviceConditions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(serviceConditionDTOS)) {
            for (ServiceConditionDTO serviceConditionDTO : serviceConditionDTOS) {
                ServiceCondition serviceCondition = new ServiceCondition();
                BeanUtils.copyProperties(serviceConditionDTO, serviceCondition);

                serviceConditions.add(serviceCondition);
            }
        }
        return serviceConditions;
    }

}
