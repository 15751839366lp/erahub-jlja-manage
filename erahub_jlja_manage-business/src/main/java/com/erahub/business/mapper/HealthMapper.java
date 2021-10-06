package com.erahub.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erahub.common.model.business.Health;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/5/7 10:16
 * @Version 1.0
 **/
public interface HealthMapper extends BaseMapper<Health> {
    /**
     * 今日是否打卡
     * @param id
     * @return
     */
    List<Health> isReport(@Param("id") Long id);
}
