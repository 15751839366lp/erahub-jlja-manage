package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.ReduceMethodDTO;
import com.erahub.common.model.asset.metadata.ReduceMethod;
import com.erahub.common.model.asset.metadata.ReduceMethod;
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
public interface ReduceMethodMapper extends BaseMapper<ReduceMethod> {
    IPage<ReduceMethod> selectReduceMethodPageList(IPage<ReduceMethod> reduceMethodIPage,
                                                 @Param("reduceMethodDTO") ReduceMethodDTO reduceMethodDTO);

    List<ReduceMethod> selectReduceMethodListByIds(@Param("reduceMethodIds") List<Long> reduceMethodIds);
}
