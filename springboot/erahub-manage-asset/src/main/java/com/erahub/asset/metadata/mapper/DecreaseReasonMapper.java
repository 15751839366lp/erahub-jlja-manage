package com.erahub.asset.metadata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erahub.common.dto.asset.metadata.DecreaseReasonDTO;
import com.erahub.common.model.asset.metadata.DecreaseReason;
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
public interface DecreaseReasonMapper extends BaseMapper<DecreaseReason> {
    IPage<DecreaseReason> selectDecreaseReasonPageList(IPage<DecreaseReason> decreaseReasonIPage,
                                                     @Param("decreaseReasonDTO") DecreaseReasonDTO decreaseReasonDTO);

    List<DecreaseReason> selectDecreaseReasonListByIds(@Param("decreaseReasonIds") List<Long> decreaseReasonIds);
}
