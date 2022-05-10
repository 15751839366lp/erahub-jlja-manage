package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.AssetOwnershipService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetOwnershipDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetOwnershipExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetOwnershipVO;
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
@RequestMapping("/asset/metadata/assetownership")
@Validated
@Api(tags = "资产管理模块-元数据管理-产权归属相关接口")
public class AssetOwnershipController {

    @Autowired
    private AssetOwnershipService assetOwnershipService;

    /**
     * 查询产权归属列表
     * @param assetOwnershipDTO
     * @return
     */
    @ApiOperation(value = "产权归属列表", notes = "查询产权归属列表")
    @RequiresPermissions({"asset:metadata:assetOwnership:select"})
    @PostMapping("/getAssetOwnershipList")
    public ResponseBean<PageVO<AssetOwnershipVO>> getAssetOwnershipList(@RequestBody AssetOwnershipDTO assetOwnershipDTO) {
        PageVO<AssetOwnershipVO> resultData = assetOwnershipService.getAssetOwnershipList(assetOwnershipDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有产权归属的excel表格")
    @GetMapping("/exportAssetOwnershipExcel")
    @RequiresPermissions("asset:metadata:assetOwnership:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出产权归属excel")
    public void exportAssetOwnershipExcel(HttpServletResponse response) throws IOException {
        List<AssetOwnershipExcel> assetOwnershipExcels = assetOwnershipService.exportAssetOwnershipExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''产权归属列表.xlsx");
        EasyExcel.write(response.getOutputStream(), AssetOwnershipExcel.class).sheet("产权归属列表").doWrite(assetOwnershipExcels);
    }

    /**
     * 更新产权归属状态
     * @param assetOwnershipId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新产权归属状态失败", operation = "产权归属|禁用/启用")
    @ApiOperation(value = "产权归属", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:assetOwnership:edit"})
    @PutMapping("/changeAssetOwnershipStatus/{assetOwnershipId}/{status}")
    public ResponseBean changeAssetOwnershipStatus(@PathVariable Long assetOwnershipId, @PathVariable Integer status) throws AssetException {
        assetOwnershipService.changeAssetOwnershipStatus(assetOwnershipId,status);
        return ResponseBean.success();
    }

    /**
     * 添加产权归属
     * @param assetOwnershipDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加产权归属失败", operation = "添加产权归属")
    @ApiOperation(value = "添加产权归属", notes = "添加产权归属")
    @RequiresPermissions({"asset:metadata:assetOwnership:add"})
    @PostMapping("/addAssetOwnership")
    public ResponseBean addAssetOwnership(@RequestBody @Validated AssetOwnershipDTO assetOwnershipDTO) throws AssetException {
        assetOwnershipService.addAssetOwnership(assetOwnershipDTO);
        return ResponseBean.success();
    }

    /**
     * 修改产权归属
     * @param assetOwnershipDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改产权归属失败", operation = "修改产权归属")
    @ApiOperation(value = "修改产权归属", notes = "修改产权归属")
    @RequiresPermissions({"asset:metadata:assetOwnership:edit"})
    @PutMapping("/updateAssetOwnership")
    public ResponseBean updateAssetOwnership(@RequestBody @Validated AssetOwnershipDTO assetOwnershipDTO) throws AssetException {
        assetOwnershipService.updateAssetOwnership(assetOwnershipDTO);
        return ResponseBean.success();
    }

    /**
     * 删除产权归属
     * @param assetOwnershipId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除产权归属失败", operation = "删除产权归属")
    @RequiresPermissions({"asset:metadata:assetOwnership:delete"})
    @ApiOperation(value = "删除产权归属", notes = "删除产权归属信息，根据产权归属ID")
    @DeleteMapping("/deleteAssetOwnership/{assetOwnershipId}")
    public ResponseBean deleteAssetOwnership(@PathVariable Long assetOwnershipId) throws AssetException {
        assetOwnershipService.deleteAssetOwnershipById(assetOwnershipId);
        return ResponseBean.success();
    }

    /**
     * 批量删除产权归属
     * @param assetOwnershipIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除产权归属失败", operation = "批量删除产权归属")
    @RequiresPermissions({"asset:metadata:assetOwnership:delete"})
    @ApiOperation(value = "批量删除产权归属", notes = "批量删除产权归属信息，根据产权归属ID")
    @DeleteMapping("/deleteAssetOwnershipByBatchId")
    public ResponseBean deleteAssetOwnershipByBatchId(@RequestBody List<Long> assetOwnershipIds) throws AssetException {
        assetOwnershipService.deleteAssetOwnershipByBatchId(assetOwnershipIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入产权归属
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入产权归属失败", operation = "上传导入产权归属")
    @RequiresPermissions({"asset:metadata:assetOwnership:import"})
    @ApiOperation(value = "上传导入产权归属", notes = "上传导入产权归属")
    @PostMapping("/importAssetOwnership")
    public ResponseBean importAssetOwnership(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        assetOwnershipService.importAssetOwnership(fileMap);
        return ResponseBean.success();
    }
}
