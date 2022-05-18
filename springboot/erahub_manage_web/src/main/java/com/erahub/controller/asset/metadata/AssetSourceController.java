package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.AssetSourceService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetSourceDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetSourceExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetSourceVO;
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
 * @since 2022-05-08
 */
@RestController
@Validated
@Api(tags = "资产管理模块-元数据管理-资产来源相关接口")
@RequestMapping("/asset/metadata/assetsource")
public class AssetSourceController {
    
    @Autowired
    private AssetSourceService assetSourceService;

    /**
     * 查询资产来源列表
     * @param assetSourceDTO
     * @return
     */
    @ApiOperation(value = "资产来源列表", notes = "查询资产来源列表")
    @RequiresPermissions({"asset:metadata:assetSource:select"})
    @PostMapping("/getAssetSourceList")
    public ResponseBean<PageVO<AssetSourceVO>> getAssetSourceList(@RequestBody AssetSourceDTO assetSourceDTO) {
        PageVO<AssetSourceVO> resultData = assetSourceService.getAssetSourceList(assetSourceDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有资产来源的excel表格")
    @GetMapping("/exportAssetSourceExcel")
    @RequiresPermissions("asset:metadata:assetSource:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出资产来源excel")
    public void exportAssetSourceExcel(HttpServletResponse response) throws IOException {
        List<AssetSourceExcel> assetSourceExcels = assetSourceService.exportAssetSourceExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''资产来源列表.xlsx");
        EasyExcel.write(response.getOutputStream(), AssetSourceExcel.class).sheet("资产来源列表").doWrite(assetSourceExcels);
    }

    /**
     * 更新资产来源状态
     * @param assetSourceId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新资产来源状态失败", operation = "资产来源|禁用/启用")
    @ApiOperation(value = "资产来源", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:assetSource:edit"})
    @PutMapping("/changeAssetSourceStatus/{assetSourceId}/{status}")
    public ResponseBean changeAssetSourceStatus(@PathVariable Long assetSourceId, @PathVariable Integer status) throws AssetException {
        assetSourceService.changeAssetSourceStatus(assetSourceId,status);
        return ResponseBean.success();
    }

    /**
     * 添加资产来源
     * @param assetSourceDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加资产来源失败", operation = "添加资产来源")
    @ApiOperation(value = "添加资产来源", notes = "添加资产来源")
    @RequiresPermissions({"asset:metadata:assetSource:add"})
    @PostMapping("/addAssetSource")
    public ResponseBean addAssetSource(@RequestBody @Validated AssetSourceDTO assetSourceDTO) throws AssetException {
        assetSourceService.addAssetSource(assetSourceDTO);
        return ResponseBean.success();
    }

    /**
     * 修改资产来源
     * @param assetSourceDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改资产来源失败", operation = "修改资产来源")
    @ApiOperation(value = "修改资产来源", notes = "修改资产来源")
    @RequiresPermissions({"asset:metadata:assetSource:edit"})
    @PutMapping("/updateAssetSource")
    public ResponseBean updateAssetSource(@RequestBody @Validated AssetSourceDTO assetSourceDTO) throws AssetException {
        assetSourceService.updateAssetSource(assetSourceDTO);
        return ResponseBean.success();
    }

    /**
     * 删除资产来源
     * @param assetSourceId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除资产来源失败", operation = "删除资产来源")
    @RequiresPermissions({"asset:metadata:assetSource:delete"})
    @ApiOperation(value = "删除资产来源", notes = "删除资产来源信息，根据资产来源ID")
    @DeleteMapping("/deleteAssetSource/{assetSourceId}")
    public ResponseBean deleteAssetSource(@PathVariable Long assetSourceId) throws AssetException {
        assetSourceService.deleteAssetSourceById(assetSourceId);
        return ResponseBean.success();
    }

    /**
     * 批量删除资产来源
     * @param assetSourceIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除资产来源失败", operation = "批量删除资产来源")
    @RequiresPermissions({"asset:metadata:assetSource:delete"})
    @ApiOperation(value = "批量删除资产来源", notes = "批量删除资产来源信息，根据资产来源ID")
    @DeleteMapping("/deleteAssetSourceByBatchId")
    public ResponseBean deleteAssetSourceByBatchId(@RequestBody List<Long> assetSourceIds) throws AssetException {
        assetSourceService.deleteAssetSourceByBatchId(assetSourceIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入资产来源
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入资产来源失败", operation = "上传导入资产来源")
    @RequiresPermissions({"asset:metadata:assetSource:import"})
    @ApiOperation(value = "上传导入资产来源", notes = "上传导入资产来源")
    @PostMapping("/importAssetSource")
    public ResponseBean importAssetSource(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        assetSourceService.importAssetSource(fileMap);
        return ResponseBean.success();
    }
}
