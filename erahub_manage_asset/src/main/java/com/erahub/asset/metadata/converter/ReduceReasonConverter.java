package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.ReduceReasonDTO;
import com.erahub.common.model.asset.metadata.ReduceReason;
import com.erahub.common.vo.asset.metadata.ReduceReasonVO;
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
public class ReduceReasonConverter {

    /**
     * pojo转vo list
     *
     * @param reduceReasons
     * @return
     */
    public static List<ReduceReasonVO> converterToReduceReasonVOList(List<ReduceReason> reduceReasons) {
        List<ReduceReasonVO> reduceReasonVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reduceReasons)) {
            for (ReduceReason reduceReason : reduceReasons) {
                ReduceReasonVO reduceReasonVO = new ReduceReasonVO();
                BeanUtils.copyProperties(reduceReason, reduceReasonVO);

                if(reduceReason.getAssetQuantity() == null){
                    reduceReasonVO.setAssetQuantity(0l);
                }
//                if(section.getSectionDetailed() != null){
//                    sectionVO.setHasChildren(section.getSectionDetailed() == 1 ? false : true);
//                }

                reduceReasonVOS.add(reduceReasonVO);
            }
        }
        return reduceReasonVOS;
    }

    /**
     * dto转pojo
     *
     * @param reduceReasonDTO
     * @param reduceReason
     * @return
     */
    public static void converterToReduceReason(ReduceReasonDTO reduceReasonDTO, ReduceReason reduceReason) {
        BeanUtils.copyProperties(reduceReasonDTO, reduceReason);
    }

    /**
     * dto转pojo list
     *
     * @param reduceReasonDTOS
     * @return
     */
    public static List<ReduceReason> converterToReduceReasonList(List<ReduceReasonDTO> reduceReasonDTOS) {
        List<ReduceReason> reduceReasons = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reduceReasonDTOS)) {
            for (ReduceReasonDTO reduceReasonDTO : reduceReasonDTOS) {
                ReduceReason reduceReason = new ReduceReason();
                BeanUtils.copyProperties(reduceReasonDTO, reduceReason);

                reduceReasons.add(reduceReason);
            }
        }
        return reduceReasons;
    }
}
