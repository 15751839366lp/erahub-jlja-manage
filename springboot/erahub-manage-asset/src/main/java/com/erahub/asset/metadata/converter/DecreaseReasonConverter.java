package com.erahub.asset.metadata.converter;

import com.erahub.common.dto.asset.metadata.DecreaseReasonDTO;
import com.erahub.common.model.asset.metadata.DecreaseReason;
import com.erahub.common.vo.asset.metadata.DecreaseReasonVO;
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
public class DecreaseReasonConverter {

    /**
     * pojo转vo list
     *
     * @param decreaseReasons
     * @return
     */
    public static List<DecreaseReasonVO> converterToDecreaseReasonVOList(List<DecreaseReason> decreaseReasons) {
        List<DecreaseReasonVO> decreaseReasonVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(decreaseReasons)) {
            for (DecreaseReason decreaseReason : decreaseReasons) {
                DecreaseReasonVO decreaseReasonVO = new DecreaseReasonVO();
                BeanUtils.copyProperties(decreaseReason, decreaseReasonVO);

                if(decreaseReason.getAssetQuantity() == null){
                    decreaseReasonVO.setAssetQuantity(0l);
                }

                decreaseReasonVOS.add(decreaseReasonVO);
            }
        }
        return decreaseReasonVOS;
    }

    /**
     * dto转pojo
     *
     * @param decreaseReasonDTO
     * @param decreaseReason
     * @return
     */
    public static void converterToDecreaseReason(DecreaseReasonDTO decreaseReasonDTO, DecreaseReason decreaseReason) {
        BeanUtils.copyProperties(decreaseReasonDTO, decreaseReason);
    }

    /**
     * dto转pojo list
     *
     * @param decreaseReasonDTOS
     * @return
     */
    public static List<DecreaseReason> converterToDecreaseReasonList(List<DecreaseReasonDTO> decreaseReasonDTOS) {
        List<DecreaseReason> decreaseReasons = new ArrayList<>();
        if (!CollectionUtils.isEmpty(decreaseReasonDTOS)) {
            for (DecreaseReasonDTO decreaseReasonDTO : decreaseReasonDTOS) {
                DecreaseReason decreaseReason = new DecreaseReason();
                BeanUtils.copyProperties(decreaseReasonDTO, decreaseReason);

                decreaseReasons.add(decreaseReason);
            }
        }
        return decreaseReasons;
    }
}
