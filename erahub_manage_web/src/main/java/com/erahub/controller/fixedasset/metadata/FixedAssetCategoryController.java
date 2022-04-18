package com.erahub.controller.fixedasset.metadata;


import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.PageVO;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询资产类别列表
     * @return
     */
    @ApiOperation(value = "资产类别列表", notes = "查询资产类别列表")
    @PostMapping("/findFixedAssetCategoryList")
    public ResponseBean<PageVO<FixedAssetCategoryVO>> findFixedAssetCateguryList(@RequestBody FixedAssetCategoryDTO fixedAssetCategoryDTO) {
        PageVO<FixedAssetCategoryVO> resultData = fixedAssetCategoryService.findFixedAssetCategoryList(fixedAssetCategoryDTO);
        return ResponseBean.success(resultData);
    }
}
