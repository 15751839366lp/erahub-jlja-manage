package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.model.asset.metadata.ServiceCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:48
 * @Version 1.0
 **/
public interface ServiceConditionMapper extends BaseMapper<ServiceCondition> {

    IPage<ServiceCondition> selectServiceConditionPageList(IPage<ServiceCondition> serviceConditionIPage,
                                                     @Param("serviceConditionDTO") ServiceConditionDTO serviceConditionDTO);

    List<ServiceCondition> selectServiceConditionListByIds(@Param("serviceConditionIds") List<Long> serviceConditionIds);

}
