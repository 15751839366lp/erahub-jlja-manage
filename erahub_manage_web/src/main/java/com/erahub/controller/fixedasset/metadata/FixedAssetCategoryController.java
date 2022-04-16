package com.erahub.controller.fixedasset.metadata;


import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/

@RestController
@RequestMapping("/fixedasset/metadata/fixedassetcategory")
@Validated
@Api(tags = "固定资产管理模块-元数据管理-资产类型相关接口")
public class FixedAssetCategoryController {

    @Autowired
    private FixedAssetCategoryService fixedAssetCategoryService;


}
