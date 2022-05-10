package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.AssetProjectService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetProjectDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetProjectExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetProjectVO;
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
@RequestMapping("/asset/metadata/assetproject")
@Validated
@Api(tags = "资产管理模块-元数据管理-工程项目相关接口")
public class AssetProjectController {

    @Autowired
    private AssetProjectService assetProjectService;

    /**
     * 查询工程项目列表
     * @param assetProjectDTO
     * @return
     */
    @ApiOperation(value = "工程项目列表", notes = "查询工程项目列表")
    @RequiresPermissions({"asset:metadata:assetProject:select"})
    @PostMapping("/getAssetProjectList")
    public ResponseBean<PageVO<AssetProjectVO>> getAssetProjectList(@RequestBody AssetProjectDTO assetProjectDTO) {
        PageVO<AssetProjectVO> resultData = assetProjectService.getAssetProjectList(assetProjectDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有工程项目的excel表格")
    @GetMapping("/exportAssetProjectExcel")
    @RequiresPermissions("asset:metadata:assetProject:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出工程项目excel")
    public void exportAssetProjectExcel(HttpServletResponse response) throws IOException {
        List<AssetProjectExcel> assetProjectExcels = assetProjectService.exportAssetProjectExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''工程项目列表.xlsx");
        EasyExcel.write(response.getOutputStream(), AssetProjectExcel.class).sheet("工程项目列表").doWrite(assetProjectExcels);
    }

    /**
     * 更新工程项目状态
     * @param assetProjectId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新工程项目状态失败", operation = "工程项目|禁用/启用")
    @ApiOperation(value = "工程项目", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:assetProject:edit"})
    @PutMapping("/changeAssetProjectStatus/{assetProjectId}/{status}")
    public ResponseBean changeAssetProjectStatus(@PathVariable String assetProjectId, @PathVariable Integer status) throws AssetException {
        assetProjectService.changeAssetProjectStatus(assetProjectId,status);
        return ResponseBean.success();
    }

    /**
     * 添加工程项目
     * @param assetProjectDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加工程项目失败", operation = "添加工程项目")
    @ApiOperation(value = "添加工程项目", notes = "添加工程项目")
    @RequiresPermissions({"asset:metadata:assetProject:add"})
    @PostMapping("/addAssetProject")
    public ResponseBean addAssetProject(@RequestBody @Validated AssetProjectDTO assetProjectDTO) throws AssetException {
        assetProjectService.addAssetProject(assetProjectDTO);
        return ResponseBean.success();
    }

    /**
     * 修改工程项目
     * @param assetProjectDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改工程项目失败", operation = "修改工程项目")
    @ApiOperation(value = "修改工程项目", notes = "修改工程项目")
    @RequiresPermissions({"asset:metadata:assetProject:edit"})
    @PutMapping("/updateAssetProject")
    public ResponseBean updateAssetProject(@RequestBody @Validated AssetProjectDTO assetProjectDTO) throws AssetException {
        assetProjectService.updateAssetProject(assetProjectDTO);
        return ResponseBean.success();
    }

    /**
     * 删除工程项目
     * @param assetProjectId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除工程项目失败", operation = "删除工程项目")
    @RequiresPermissions({"asset:metadata:assetProject:delete"})
    @ApiOperation(value = "删除工程项目", notes = "删除工程项目信息，根据工程项目ID")
    @DeleteMapping("/deleteAssetProject/{assetProjectId}")
    public ResponseBean deleteAssetProject(@PathVariable String assetProjectId) throws AssetException {
        assetProjectService.deleteAssetProjectById(assetProjectId);
        return ResponseBean.success();
    }

    /**
     * 批量删除工程项目
     * @param assetProjectIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除工程项目失败", operation = "批量删除工程项目")
    @RequiresPermissions({"asset:metadata:assetProject:delete"})
    @ApiOperation(value = "批量删除工程项目", notes = "批量删除工程项目信息，根据工程项目ID")
    @DeleteMapping("/deleteAssetProjectByBatchId")
    public ResponseBean deleteAssetProjectByBatchId(@RequestBody List<String> assetProjectIds) throws AssetException {
        assetProjectService.deleteAssetProjectByBatchId(assetProjectIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入工程项目
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入工程项目失败", operation = "上传导入工程项目")
    @RequiresPermissions({"asset:metadata:assetProject:import"})
    @ApiOperation(value = "上传导入工程项目", notes = "上传导入工程项目")
    @PostMapping("/importAssetProject")
    public ResponseBean importAssetProject(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        assetProjectService.importAssetProject(fileMap);
        return ResponseBean.success();
    }
}
