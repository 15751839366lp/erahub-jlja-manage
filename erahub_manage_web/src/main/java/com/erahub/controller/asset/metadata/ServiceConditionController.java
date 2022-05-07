package com.erahub.controller.asset.metadata;

import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.ServiceConditionService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.AssetCategoryDTO;

import com.erahub.common.dto.asset.metadata.ServiceConditionDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.AssetCategoryExcel;
import com.erahub.common.excel.model.asset.metadata.ServiceConditionExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import com.erahub.common.vo.asset.metadata.ServiceConditionVO;
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
 * @Date 2022/5/7 9:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/asset/metadata/servicecondition")
@Validated
@Api(tags = "资产管理模块-元数据管理-使用状态相关接口")
public class ServiceConditionController {

    @Autowired
    private ServiceConditionService serviceConditionService;

    /**
     * 查询使用状态列表
     * @param serviceConditionDTO
     * @return
     */
    @ApiOperation(value = "使用状态列表", notes = "查询使用状态列表")
    @RequiresPermissions({"asset:metadata:serviceCondition:select"})
    @PostMapping("/getServiceConditionList")
    public ResponseBean<PageVO<ServiceConditionVO>> getServiceConditionList(@RequestBody ServiceConditionDTO serviceConditionDTO) {
        PageVO<ServiceConditionVO> resultData = serviceConditionService.getServiceConditionList(serviceConditionDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有使用状态的excel表格")
    @GetMapping("/exportServiceConditionExcel")
    @RequiresPermissions("asset:metadata:serviceCondition:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出使用状态excel")
    public void exportServiceConditionExcel(HttpServletResponse response) throws IOException {
        List<ServiceConditionExcel> serviceConditionExcels = serviceConditionService.exportServiceConditionExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''使用状态列表.xlsx");
        EasyExcel.write(response.getOutputStream(), ServiceConditionExcel.class).sheet("使用状态列表").doWrite(serviceConditionExcels);
    }

    /**
     * 更新使用状态状态
     * @param serviceConditionId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新使用状态状态失败", operation = "使用状态|禁用/启用")
    @ApiOperation(value = "使用状态", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:edit"})
    @PutMapping("/changeServiceConditionStatus/{serviceConditionId}/{status}")
    public ResponseBean changeServiceConditionStatus(@PathVariable Long serviceConditionId, @PathVariable Long status) throws AssetException {
        serviceConditionService.changeServiceConditionStatus(serviceConditionId,status);
        return ResponseBean.success();
    }

    /**
     * 添加使用状态
     * @param serviceConditionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加使用状态失败", operation = "添加使用状态")
    @ApiOperation(value = "添加使用状态", notes = "添加使用状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:add"})
    @PostMapping("/addServiceCondition")
    public ResponseBean addServiceCondition(@RequestBody @Validated ServiceConditionDTO serviceConditionDTO) throws AssetException {
        serviceConditionService.addServiceCondition(serviceConditionDTO);
        return ResponseBean.success();
    }

    /**
     * 修改使用状态
     * @param serviceConditionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改使用状态失败", operation = "修改使用状态")
    @ApiOperation(value = "修改使用状态", notes = "修改使用状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:edit"})
    @PutMapping("/updateServiceCondition")
    public ResponseBean updateServiceCondition(@RequestBody @Validated ServiceConditionDTO serviceConditionDTO) throws AssetException {
        serviceConditionService.updateServiceCondition(serviceConditionDTO);
        return ResponseBean.success();
    }

    /**
     * 删除使用状态
     * @param serviceConditionId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除使用状态失败", operation = "删除使用状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:delete"})
    @ApiOperation(value = "删除使用状态", notes = "删除使用状态信息，根据使用状态ID")
    @DeleteMapping("/deleteServiceCondition/{serviceConditionId}")
    public ResponseBean deleteServiceCondition(@PathVariable Long serviceConditionId) throws AssetException {
        serviceConditionService.deleteServiceConditionById(serviceConditionId);
        return ResponseBean.success();
    }

    /**
     * 批量删除使用状态
     * @param serviceConditionIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除使用状态失败", operation = "批量删除使用状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:delete"})
    @ApiOperation(value = "批量删除使用状态", notes = "批量删除使用状态信息，根据使用状态ID")
    @DeleteMapping("/deleteServiceConditionByBatchId")
    public ResponseBean deleteServiceConditionByBatchId(@RequestBody List<Long> serviceConditionIds) throws AssetException {
        serviceConditionService.deleteServiceConditionByBatchId(serviceConditionIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入使用状态
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入使用状态失败", operation = "上传导入使用状态")
    @RequiresPermissions({"asset:metadata:serviceCondition:import"})
    @ApiOperation(value = "上传导入使用状态", notes = "上传导入使用状态")
    @PostMapping("/importServiceCondition")
    public ResponseBean importServiceCondition(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        serviceConditionService.importServiceCondition(fileMap);
        return ResponseBean.success();
    }
}
