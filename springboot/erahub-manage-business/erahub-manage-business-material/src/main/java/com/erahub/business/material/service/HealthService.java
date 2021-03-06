package com.erahub.business.material.service;


import com.erahub.common.error.business.material.BusinessException;
import com.erahub.common.model.business.material.Health;
import com.erahub.common.vo.business.material.HealthVO;
import com.erahub.common.vo.common.PageVO;

/**
 * @Author lipeng
 * @Date 2020/5/7 10:20
 * @Version 1.0
 **/
public interface HealthService {

    /**
     * 健康上报
     * @param healthVO
     */
    void report(HealthVO healthVO) throws BusinessException;

    /**
     * 今日是否已经报备
     * @param id
     * @return
     */
    Health isReport(Long id);

    /**
     * 签到记录
     * @return
     */
    PageVO<Health> history(Long id, Integer pageNum, Integer pageSize);
}
