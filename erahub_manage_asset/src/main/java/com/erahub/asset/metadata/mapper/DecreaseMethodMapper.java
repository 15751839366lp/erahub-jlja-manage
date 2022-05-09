package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.DecreaseMethodDTO;
import com.erahub.common.model.asset.metadata.DecreaseMethod;
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
public interface DecreaseMethodMapper extends BaseMapper<DecreaseMethod> {
    IPage<DecreaseMethod> selectDecreaseMethodPageList(IPage<DecreaseMethod> decreaseMethodIPage,
                                                     @Param("decreaseMethodDTO") DecreaseMethodDTO decreaseMethodDTO);

    List<DecreaseMethod> selectDecreaseMethodListByIds(@Param("decreaseMethodIds") List<Long> decreaseMethodIds);
}
