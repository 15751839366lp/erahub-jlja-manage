package com.erahub.controller.fixedasset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.error.system.SystemException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.system.UserVO;
import com.erahub.fixedasset.metadata.service.FixedAssetCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有资产类别的excel表格")
    @GetMapping("/exportFixedAssetCategoryExcel")
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
     * 添加资产类别
     * @param fixedAssetCategoryDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加资产类别失败", operation = "添加资产类别")
    @ApiOperation(value = "添加资产类别", notes = "添加资产类别")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:add"})
    @PostMapping("/addFixedAssetCategory")
    public ResponseBean addFixedAssetCategory(@RequestBody @Validated FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException {
        fixedAssetCategoryService.addFixedAssetCategory(fixedAssetCategoryDTO);
        return ResponseBean.success();
    }

    /**
     * 修改资产类别
     * @param fixedAssetCategoryDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改资产类别失败", operation = "修改资产类别")
    @ApiOperation(value = "修改资产类别", notes = "修改资产类别")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:edit"})
    @PutMapping("/updateFixedAssetCategory")
    public ResponseBean updateFixedAssetCategory(@RequestBody @Validated FixedAssetCategoryDTO fixedAssetCategoryDTO) throws FixedAssetException {
        fixedAssetCategoryService.updateFixedAssetCategory(fixedAssetCategoryDTO);
        return ResponseBean.success();
    }

    /**
     * 删除资产类别
     * @param id
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除资产类别失败", operation = "删除资产类别")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:delete"})
    @ApiOperation(value = "删除资产类别", notes = "删除资产类别信息，根据资产类别ID")
    @DeleteMapping("/deleteFixedAssetCategory/{id}")
    public ResponseBean deleteFixedAssetCategory(@PathVariable Long id) throws FixedAssetException {
        fixedAssetCategoryService.deleteFixedAssetCategoryById(id);
        return ResponseBean.success();
    }

    /**
     * 批量删除资产类别
     * @param categoryIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除资产类别失败", operation = "批量删除资产类别")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:delete"})
    @ApiOperation(value = "批量删除资产类别", notes = "批量删除资产类别信息，根据资产类别ID")
    @DeleteMapping("/deleteFixedAssetCategoryByBatchId")
    public ResponseBean deleteFixedAssetCategoryByBatchId(@RequestBody List<Long> categoryIds) throws FixedAssetException {
        fixedAssetCategoryService.deleteFixedAssetCategoryByBatchId(categoryIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入资产类别
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入资产类别失败", operation = "上传导入资产类别")
    @RequiresPermissions({"fixedAsset:metadata:fixedAssetCategory:import"})
    @ApiOperation(value = "上传导入资产类别", notes = "上传导入资产类别")
    @PostMapping("/importFixedAssetCategory")
    public ResponseBean importFixedAssetCategory(HttpServletRequest request) throws FixedAssetException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        fixedAssetCategoryService.importFixedAssetCategory(fileMap);
        return ResponseBean.success();
    }
}
