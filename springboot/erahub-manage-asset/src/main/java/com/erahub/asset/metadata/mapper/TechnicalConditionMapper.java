package com.erahub.asset.metadata.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.TechnicalConditionDTO;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import com.erahub.common.model.asset.metadata.TechnicalCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lipeng
 * @since 2022-05-08
 */
public interface TechnicalConditionMapper extends BaseMapper<TechnicalCondition> {
    IPage<TechnicalCondition> selectTechnicalConditionPageList(IPage<TechnicalCondition> technicalConditionIPage,
                                                   @Param("technicalConditionDTO") TechnicalConditionDTO technicalConditionDTO);

    List<TechnicalCondition> selectTechnicalConditionListByIds(@Param("technicalConditionIds") List<Long> technicalConditionIds);
}
