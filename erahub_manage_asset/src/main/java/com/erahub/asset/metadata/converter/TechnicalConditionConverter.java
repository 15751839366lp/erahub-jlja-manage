package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.TechnicalConditionDTO;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import com.erahub.common.vo.asset.metadata.TechnicalConditionVO;
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
public class TechnicalConditionConverter {

    /**
     * pojo转vo list
     *
     * @param technicalConditions
     * @return
     */
    public static List<TechnicalConditionVO> converterToTechnicalConditionVOList(List<TechnicalCondition> technicalConditions) {
        List<TechnicalConditionVO> technicalConditionVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(technicalConditions)) {
            for (TechnicalCondition technicalCondition : technicalConditions) {
                TechnicalConditionVO technicalConditionVO = new TechnicalConditionVO();
                BeanUtils.copyProperties(technicalCondition, technicalConditionVO);

                if(technicalCondition.getAssetQuantity() == null){
                    technicalConditionVO.setAssetQuantity(0l);
                }
//                if(section.getSectionDetailed() != null){
//                    sectionVO.setHasChildren(section.getSectionDetailed() == 1 ? false : true);
//                }

                technicalConditionVOS.add(technicalConditionVO);
            }
        }
        return technicalConditionVOS;
    }

    /**
     * dto转pojo
     *
     * @param technicalConditionDTO
     * @param technicalCondition
     * @return
     */
    public static void converterToTechnicalCondition(TechnicalConditionDTO technicalConditionDTO, TechnicalCondition technicalCondition) {
        BeanUtils.copyProperties(technicalConditionDTO, technicalCondition);
    }

    /**
     * dto转pojo list
     *
     * @param technicalConditionDTOS
     * @return
     */
    public static List<TechnicalCondition> converterToTechnicalConditionList(List<TechnicalConditionDTO> technicalConditionDTOS) {
        List<TechnicalCondition> technicalConditions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(technicalConditionDTOS)) {
            for (TechnicalConditionDTO technicalConditionDTO : technicalConditionDTOS) {
                TechnicalCondition technicalCondition = new TechnicalCondition();
                BeanUtils.copyProperties(technicalConditionDTO, technicalCondition);

                technicalConditions.add(technicalCondition);
            }
        }
        return technicalConditions;
    }
}
