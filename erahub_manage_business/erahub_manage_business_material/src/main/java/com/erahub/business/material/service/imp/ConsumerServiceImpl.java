package com.erahub.business.material.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erahub.business.material.mapper.ConsumerMapper;
import com.erahub.business.material.service.ConsumerService;
import com.erahub.business.material.converter.ConsumerConverter;
import com.erahub.common.model.business.material.Consumer;
import com.erahub.common.vo.business.material.ConsumerVO;
import com.erahub.common.vo.common.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/3/16 17:19
 * @Version 1.0
 **/
@Service
public class ConsumerServiceImpl implements ConsumerService {


    @Autowired
    private ConsumerMapper consumerMapper;

    /**
     * 供应商列表
     * @param pageNum
     * @param pageSize
     * @param consumerVO
     * @return
     */
    @Override
    public PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVO consumerVO) {
        IPage<Consumer> consumerIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
        consumerQueryWrapper.orderByAsc("sort");

        if (consumerVO.getName() != null && !"".equals(consumerVO.getName())) {
            consumerQueryWrapper.like("name", consumerVO.getName());
        }
        if (consumerVO.getAddress() != null && !"".equals(consumerVO.getAddress())) {
            consumerQueryWrapper.like("address", consumerVO.getAddress());
        }
        if (consumerVO.getContact() != null && !"".equals(consumerVO.getContact())) {
            consumerQueryWrapper.like("contact", consumerVO.getContact());
        }
        consumerIPage = consumerMapper.selectPage(consumerIPage, consumerQueryWrapper);
        List<Consumer> consumers = consumerIPage.getRecords();
        List<ConsumerVO> categoryVOS= ConsumerConverter.converterToVOList(consumers);

        return new PageVO<>(consumerIPage.getTotal(), categoryVOS);
    }



    /**
     * 添加供应商
     * @param ConsumerVO
     */
    @Override
    public Consumer add(ConsumerVO ConsumerVO) {
        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(ConsumerVO,consumer);
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        consumerMapper.insert(consumer);
        return consumer;
    }

    /**
     * 编辑供应商
     * @param id
     * @return
     */
    @Override
    public ConsumerVO edit(Long id) {
        Consumer consumer = consumerMapper.selectById(id);
        return  ConsumerConverter.converterToConsumerVO(consumer);
    }

    /**
     * 更新供应商
     * @param id
     * @param ConsumerVO
     */
    @Override
    public void update(Long id, ConsumerVO ConsumerVO) {
        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(ConsumerVO,consumer);
        consumer.setModifiedTime(new Date());
        consumerMapper.updateById(consumer);
    }

    /**
     * 删除供应商
     * @param id
     */
    @Override
    public void delete(Long id) {
        consumerMapper.deleteById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<ConsumerVO> findAll() {
        List<Consumer> consumers = consumerMapper.selectList(null);
        return ConsumerConverter.converterToVOList(consumers);
    }

}
