package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.ReduceReasonDTO;
import com.erahub.common.model.asset.metadata.ReduceReason;
import com.erahub.common.model.asset.metadata.ReduceReason;
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
public interface ReduceReasonMapper extends BaseMapper<ReduceReason> {
    IPage<ReduceReason> selectReduceReasonPageList(IPage<ReduceReason> reduceReasonIPage,
                                                   @Param("reduceReasonDTO") ReduceReasonDTO reduceReasonDTO);

    List<ReduceReason> selectReduceReasonListByIds(@Param("reduceReasonIds") List<Long> reduceReasonIds);
}
