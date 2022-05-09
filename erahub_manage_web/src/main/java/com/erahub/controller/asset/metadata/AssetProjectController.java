package com.erahub.controller.asset.metadata;


import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lipeng
 * @since 2022-05-09
 */
@RestController
@RequestMapping("/asset/metadata/assetproject")
@Validated
@Api(tags = "资产管理模块-元数据管理-工程项目相关接口")
public class AssetProjectController {

}
