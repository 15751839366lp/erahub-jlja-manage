package com.erahub;

import com.erahub.common.model.asset.metadata.DepreciationMethod;
import com.erahub.asset.metadata.mapper.DepreciationMethodMapper;
import com.erahub.asset.metadata.service.DepreciationMethodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/21 18:35
 * @Version 1.0
 **/
@SpringBootTest
public class DepreciationMethodTest {

    @Autowired
    private DepreciationMethodService depreciationMethodService;

    @Autowired
    private DepreciationMethodMapper depreciationMethodMapper;

    @Test
    void getDepreciationMethodList() {
        List<DepreciationMethod> depreciationMethods = depreciationMethodMapper.selectList(null);
        System.out.println(depreciationMethods);
    }
}
