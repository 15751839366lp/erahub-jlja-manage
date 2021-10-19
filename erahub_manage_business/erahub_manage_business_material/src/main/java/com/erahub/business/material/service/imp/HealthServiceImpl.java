package com.erahub.business.material.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.business.material.mapper.HealthMapper;
import com.erahub.business.material.service.HealthService;
import com.erahub.common.error.business.BusinessCodeEnum;
import com.erahub.common.error.business.BusinessException;
import com.erahub.common.model.business.Health;
import com.erahub.common.vo.business.HealthVO;
import com.erahub.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/5/7 10:21
 * @Version 1.0
 **/
@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthMapper healthMapper;

    /**
     * 健康上报
     * @param healthVO
     */
    @Override
    public void report(HealthVO healthVO) throws BusinessException {
        Health report = isReport(healthVO.getUserId());
        if(report!=null) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "今日已经打卡,无法重复打卡！");
        }
        Health health = new Health();
        BeanUtils.copyProperties(healthVO,health);
        health.setCreateTime(new Date());
        healthMapper.insert(health);
    }

    /**
     * 今日是否已报备
     * @param id
     * @return
     */
    @Override
    public Health isReport(Long id) {
        List<Health> health=healthMapper.isReport(id);
        if(health.size()>0){
            return  health.get(0);
        }
        return null;
    }

    /**
     * 签到历史记录
     * @return
     */
    @Override
    public PageVO<Health> history(Long id,Integer pageNum,Integer pageSize) {
        IPage<Health> healthIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Health> healthQueryWrapper = new QueryWrapper<>();
        healthQueryWrapper.orderByDesc("create_time");
        healthQueryWrapper.eq("user_id",id);

        healthIPage = healthMapper.selectPage(healthIPage, healthQueryWrapper);
        List<Health> health = healthIPage.getRecords();
        return new PageVO<>(healthIPage.getTotal(),health);
    }
}
