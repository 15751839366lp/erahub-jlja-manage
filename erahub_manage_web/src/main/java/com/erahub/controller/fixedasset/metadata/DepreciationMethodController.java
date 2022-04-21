package com.erahub.controller.fixedasset.metadata;

import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.system.DepartmentVO;
import com.erahub.fixedasset.metadata.service.DepreciationMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/21 19:26
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fixedasset/metadata/depreciationmethod")
@Validated
@Api(tags = "固定资产管理模块-元数据管理-折旧方法相关接口")
public class DepreciationMethodController {

    @Autowired
    DepreciationMethodService depreciationMethodService;

    /**
     * 所有折旧方法
     *
     * @return
     */
    @ApiOperation(value = "所有折旧方法")
    @GetMapping("/getAllDepreciationMethod")
    public ResponseBean<List<DepreciationMethod>> getAllDepreciationMethod() {
        List<DepreciationMethod> depreciationMethods = depreciationMethodService.list();
        return ResponseBean.success(depreciationMethods);
    }
}
