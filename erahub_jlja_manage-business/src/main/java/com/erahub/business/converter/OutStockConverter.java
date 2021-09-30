package com.erahub.business.converter;

import com.erahub.business.mapper.ConsumerMapper;
import com.erahub.common.model.business.Consumer;
import com.erahub.common.model.business.OutStock;
import com.erahub.common.vo.business.OutStockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2020/5/10 14:32
 * @Version 1.0
 **/
@Component
public class OutStockConverter {

    @Autowired
    private ConsumerMapper consumerMapper;

    /**
     * 转voList
     * @param outStocks
     * @return
     */
    public  List<OutStockVO> converterToVOList(List<OutStock> outStocks) {
        List<OutStockVO> outStockVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(outStocks)){
            for (OutStock outStock : outStocks) {
                OutStockVO outStockVO = new OutStockVO();
                BeanUtils.copyProperties(outStock,outStockVO);
                Consumer consumer = consumerMapper.selectByPrimaryKey(outStock.getConsumerId());
                if(consumer!=null){
                    outStockVO.setName(consumer.getName());
                    outStockVO.setPhone(consumer.getPhone());
                }
                outStockVOS.add(outStockVO);
            }
        }
        return outStockVOS;
    }
}
