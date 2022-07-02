package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.AssetEconomicUseService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetEconomicUseDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetEconomicUseExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetEconomicUseVO;
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
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lipeng
 * @since 2022-05-09
 */
@RestController
@RequestMapping("/asset/metadata/asseteconomicuse")
@Validated
@Api(tags = "资产管理模块-元数据管理-经济用途相关接口")
public class AssetEconomicUseController {

    @Autowired
    private AssetEconomicUseService assetEconomicUseService;

    /**
     * 查询经济用途列表
     * @param assetEconomicUseDTO
     * @return
     */
    @ApiOperation(value = "经济用途列表", notes = "查询经济用途列表")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:select"})
    @PostMapping("/getAssetEconomicUseList")
    public ResponseBean<PageVO<AssetEconomicUseVO>> getAssetEconomicUseList(@RequestBody AssetEconomicUseDTO assetEconomicUseDTO) {
        PageVO<AssetEconomicUseVO> resultData = assetEconomicUseService.getAssetEconomicUseList(assetEconomicUseDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有经济用途的excel表格")
    @GetMapping("/exportAssetEconomicUseExcel")
    @RequiresPermissions("asset:metadata:assetEconomicUse:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出经济用途excel")
    public void exportAssetEconomicUseExcel(HttpServletResponse response) throws IOException {
        List<AssetEconomicUseExcel> assetEconomicUseExcels = assetEconomicUseService.exportAssetEconomicUseExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''经济用途列表.xlsx");
        EasyExcel.write(response.getOutputStream(), AssetEconomicUseExcel.class).sheet("经济用途列表").doWrite(assetEconomicUseExcels);
    }

    /**
     * 更新经济用途状态
     * @param assetEconomicUseId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新经济用途状态失败", operation = "经济用途|禁用/启用")
    @ApiOperation(value = "经济用途", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:edit"})
    @PutMapping("/changeAssetEconomicUseStatus/{assetEconomicUseId}/{status}")
    public ResponseBean changeAssetEconomicUseStatus(@PathVariable Long assetEconomicUseId, @PathVariable Integer status) throws AssetException {
        assetEconomicUseService.changeAssetEconomicUseStatus(assetEconomicUseId,status);
        return ResponseBean.success();
    }

    /**
     * 添加经济用途
     * @param assetEconomicUseDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加经济用途失败", operation = "添加经济用途")
    @ApiOperation(value = "添加经济用途", notes = "添加经济用途")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:add"})
    @PostMapping("/addAssetEconomicUse")
    public ResponseBean addAssetEconomicUse(@RequestBody @Validated AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException {
        assetEconomicUseService.addAssetEconomicUse(assetEconomicUseDTO);
        return ResponseBean.success();
    }

    /**
     * 修改经济用途
     * @param assetEconomicUseDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改经济用途失败", operation = "修改经济用途")
    @ApiOperation(value = "修改经济用途", notes = "修改经济用途")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:edit"})
    @PutMapping("/updateAssetEconomicUse")
    public ResponseBean updateAssetEconomicUse(@RequestBody @Validated AssetEconomicUseDTO assetEconomicUseDTO) throws AssetException {
        assetEconomicUseService.updateAssetEconomicUse(assetEconomicUseDTO);
        return ResponseBean.success();
    }

    /**
     * 删除经济用途
     * @param assetEconomicUseId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除经济用途失败", operation = "删除经济用途")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:delete"})
    @ApiOperation(value = "删除经济用途", notes = "删除经济用途信息，根据经济用途ID")
    @DeleteMapping("/deleteAssetEconomicUse/{assetEconomicUseId}")
    public ResponseBean deleteAssetEconomicUse(@PathVariable Long assetEconomicUseId) throws AssetException {
        assetEconomicUseService.deleteAssetEconomicUseById(assetEconomicUseId);
        return ResponseBean.success();
    }

    /**
     * 批量删除经济用途
     * @param assetEconomicUseIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除经济用途失败", operation = "批量删除经济用途")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:delete"})
    @ApiOperation(value = "批量删除经济用途", notes = "批量删除经济用途信息，根据经济用途ID")
    @DeleteMapping("/deleteAssetEconomicUseByBatchId")
    public ResponseBean deleteAssetEconomicUseByBatchId(@RequestBody List<Long> assetEconomicUseIds) throws AssetException {
        assetEconomicUseService.deleteAssetEconomicUseByBatchId(assetEconomicUseIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入经济用途
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入经济用途失败", operation = "上传导入经济用途")
    @RequiresPermissions({"asset:metadata:assetEconomicUse:import"})
    @ApiOperation(value = "上传导入经济用途", notes = "上传导入经济用途")
    @PostMapping("/importAssetEconomicUse")
    public ResponseBean importAssetEconomicUse(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        assetEconomicUseService.importAssetEconomicUse(fileMap);
        return ResponseBean.success();
    }
}
