package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.AssetCategoryService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetCategoryExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import com.erahub.common.vo.common.PageVO;
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
import java.util.List;
import java.util.Map;

/**
 * @Author lipeng
 * @Date 2022/4/16 15:15
 * @Version 1.0
 **/

@RestController
@RequestMapping("/asset/metadata/assetcategory")
@Validated
@Api(tags = "资产管理模块-元数据管理-资产类型相关接口")
public class AssetCategoryController {

    @Autowired
    private AssetCategoryService assetCategoryService;

    /**
     * 查询资产类别列表
     * @return
     */
    @ApiOperation(value = "资产类别列表", notes = "查询资产类别列表")
    @RequiresPermissions({"asset:metadata:assetCategory:select"})
    @PostMapping("/getAssetCategoryList")
    public ResponseBean<PageVO<AssetCategoryVO>> getAssetCategoryList(@RequestBody AssetCategoryDTO assetCategoryDTO) {
        PageVO<AssetCategoryVO> resultData = assetCategoryService.getAssetCategoryList(assetCategoryDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有资产类别的excel表格")
    @GetMapping("/exportAssetCategoryExcel")
    @RequiresPermissions("asset:metadata:assetCategory:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出资产类别excel")
    public void exportAssetCategoryExcel(HttpServletResponse response) throws IOException {
        List<AssetCategoryExcel> assetCategoryExcels = assetCategoryService.exportAssetCategoryExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''资产类别列表.xlsx");
        EasyExcel.write(response.getOutputStream(), AssetCategoryExcel.class).sheet("资产类别列表").doWrite(assetCategoryExcels);
    }

    /**
     * 更新资产类别状态
     * @param assetCategoryId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新资产类别状态失败", operation = "资产类别|禁用/启用")
    @ApiOperation(value = "资产类别", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:assetCategory:edit"})
    @PutMapping("/changeAssetCategoryStatus/{assetCategoryId}/{status}")
    public ResponseBean changeAssetCategoryStatus(@PathVariable String assetCategoryId, @PathVariable Long status) throws AssetException {
        assetCategoryService.changeAssetCategoryStatus(assetCategoryId,status);
        return ResponseBean.success();
    }

    /**
     * 添加资产类别
     * @param assetCategoryDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加资产类别失败", operation = "添加资产类别")
    @ApiOperation(value = "添加资产类别", notes = "添加资产类别")
    @RequiresPermissions({"asset:metadata:assetCategory:add"})
    @PostMapping("/addAssetCategory")
    public ResponseBean addAssetCategory(@RequestBody @Validated AssetCategoryDTO assetCategoryDTO) throws AssetException {
        assetCategoryService.addAssetCategory(assetCategoryDTO);
        return ResponseBean.success();
    }

    /**
     * 修改资产类别
     * @param assetCategoryDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改资产类别失败", operation = "修改资产类别")
    @ApiOperation(value = "修改资产类别", notes = "修改资产类别")
    @RequiresPermissions({"asset:metadata:assetCategory:edit"})
    @PutMapping("/updateAssetCategory")
    public ResponseBean updateAssetCategory(@RequestBody @Validated AssetCategoryDTO assetCategoryDTO) throws AssetException {
        assetCategoryService.updateAssetCategory(assetCategoryDTO);
        return ResponseBean.success();
    }

    /**
     * 删除资产类别
     * @param assetCategoryId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除资产类别失败", operation = "删除资产类别")
    @RequiresPermissions({"asset:metadata:assetCategory:delete"})
    @ApiOperation(value = "删除资产类别", notes = "删除资产类别信息，根据资产类别ID")
    @DeleteMapping("/deleteAssetCategory/{assetCategoryId}")
    public ResponseBean deleteAssetCategory(@PathVariable String assetCategoryId) throws AssetException {
        assetCategoryService.deleteAssetCategoryById(assetCategoryId);
        return ResponseBean.success();
    }

    /**
     * 批量删除资产类别
     * @param assetCategoryIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除资产类别失败", operation = "批量删除资产类别")
    @RequiresPermissions({"asset:metadata:assetCategory:delete"})
    @ApiOperation(value = "批量删除资产类别", notes = "批量删除资产类别信息，根据资产类别ID")
    @DeleteMapping("/deleteAssetCategoryByBatchId")
    public ResponseBean deleteAssetCategoryByBatchId(@RequestBody List<String> assetCategoryIds) throws AssetException {
        assetCategoryService.deleteAssetCategoryByBatchId(assetCategoryIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入资产类别
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入资产类别失败", operation = "上传导入资产类别")
    @RequiresPermissions({"asset:metadata:assetCategory:import"})
    @ApiOperation(value = "上传导入资产类别", notes = "上传导入资产类别")
    @PostMapping("/importAssetCategory")
    public ResponseBean importAssetCategory(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        assetCategoryService.importAssetCategory(fileMap);
        return ResponseBean.success();
    }
}
