package com.erahub.controller.fixedasset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    @PostMapping("/getFixedAssetCategoryList")
    public ResponseBean<PageVO<FixedAssetCategoryVO>> getFixedAssetCateguryList(@RequestBody FixedAssetCategoryDTO fixedAssetCategoryDTO) {
        PageVO<FixedAssetCategoryVO> resultData = fixedAssetCategoryService.getFixedAssetCategoryList(fixedAssetCategoryDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 更新资产类别状态
     * @param categoryId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新资产类别状态失败", operation = "资产类别|禁用/启用")
    @ApiOperation(value = "固定资产类别", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:edit"})
    @PutMapping("/changeFixedAssetCategoryStatus/{categoryId}/{status}")
    public ResponseBean changeFixedAssetCategoryStatus(@PathVariable Long categoryId, @PathVariable Boolean status) throws FixedAssetException {
        fixedAssetCategoryService.changeFixedAssetCategoryStatus(categoryId,status);
        return ResponseBean.success();
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有资产类别的excel表格")
    @PostMapping("/exportFixedAssetCategoryExcel")
    @RequiresPermissions("fixedAsset:metadata:fixedAssetCategory:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出资产类别excel")
    public void exportFixedAssetCategoryExcel(HttpServletResponse response) throws IOException {
        List<FixedAssetCategoryExcel> fixedAssetCategoryExcels = fixedAssetCategoryService.exportFixedAssetCategoryExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''资产类别列表.xlsx");
        EasyExcel.write(response.getOutputStream(), FixedAssetCategoryExcel.class).sheet("资产类别列表").doWrite(fixedAssetCategoryExcels);
    }

}
