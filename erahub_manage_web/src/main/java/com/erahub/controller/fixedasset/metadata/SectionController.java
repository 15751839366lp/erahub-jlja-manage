package com.erahub.controller.fixedasset.metadata;

import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import com.erahub.fixedasset.metadata.service.SectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:13
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fixedasset/metadata/section")
@Validated
@Api(tags = "固定资产管理模块-元数据管理-单位相关接口")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /**
     * 获取单位列表
     * @param sectionDTO
     * @return
     */
    @ApiOperation(value = "单位列表", notes = "获取单位列表")
    @RequiresPermissions({"fixedAsset:metadata:section:select"})
    @PostMapping("/getSectionList")
    public ResponseBean<PageVO<SectionVO>> getSectionList(@RequestBody SectionDTO sectionDTO) {
        PageVO<SectionVO> resultData = sectionService.getSectionList(sectionDTO);
        return ResponseBean.success(resultData);
    }

}
