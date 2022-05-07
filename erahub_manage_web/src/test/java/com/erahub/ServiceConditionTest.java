package com.erahub;

import com.erahub.asset.metadata.mapper.AssetCategoryMapper;
import com.erahub.asset.metadata.mapper.ServiceConditionMapper;
import com.erahub.asset.metadata.service.ServiceConditionService;
import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.vo.asset.metadata.ServiceConditionVO;
import com.erahub.common.vo.common.PageVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:58
 * @Version 1.0
 **/
@SpringBootTest
public class ServiceConditionTest {

    @Autowired
    private ServiceConditionService serviceConditionService;

    @Autowired
    private ServiceConditionMapper serviceConditionMapper;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Test
    void getList(){

        ServiceConditionDTO serviceConditionDTO = new ServiceConditionDTO();
        serviceConditionDTO.setPageNum(1);
        serviceConditionDTO.setPageSize(10);

        PageVO<ServiceConditionVO> serviceConditionList = serviceConditionService.getServiceConditionList(serviceConditionDTO);
        System.out.println(serviceConditionList);
    }

}
