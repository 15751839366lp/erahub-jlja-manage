package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.TechnicalConditionService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.TechnicalConditionDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.TechnicalConditionExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.TechnicalConditionVO;
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
@Api(tags = "资产管理模块-元数据管理-技术状态相关接口")
@RequestMapping("/asset/metadata/technicalcondition")
public class TechnicalConditionController {
    
    @Autowired
    private TechnicalConditionService technicalConditionService;

    /**
     * 查询技术状态列表
     * @param technicalConditionDTO
     * @return
     */
    @ApiOperation(value = "技术状态列表", notes = "查询技术状态列表")
    @RequiresPermissions({"asset:metadata:technicalCondition:select"})
    @PostMapping("/getTechnicalConditionList")
    public ResponseBean<PageVO<TechnicalConditionVO>> getTechnicalConditionList(@RequestBody TechnicalConditionDTO technicalConditionDTO) {
        PageVO<TechnicalConditionVO> resultData = technicalConditionService.getTechnicalConditionList(technicalConditionDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有技术状态的excel表格")
    @GetMapping("/exportTechnicalConditionExcel")
    @RequiresPermissions("asset:metadata:technicalCondition:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出技术状态excel")
    public void exportTechnicalConditionExcel(HttpServletResponse response) throws IOException {
        List<TechnicalConditionExcel> technicalConditionExcels = technicalConditionService.exportTechnicalConditionExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''技术状态列表.xlsx");
        EasyExcel.write(response.getOutputStream(), TechnicalConditionExcel.class).sheet("技术状态列表").doWrite(technicalConditionExcels);
    }

    /**
     * 更新技术状态状态
     * @param technicalConditionId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新技术状态状态失败", operation = "技术状态|禁用/启用")
    @ApiOperation(value = "技术状态", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:edit"})
    @PutMapping("/changeTechnicalConditionStatus/{technicalConditionId}/{status}")
    public ResponseBean changeTechnicalConditionStatus(@PathVariable Long technicalConditionId, @PathVariable Integer status) throws AssetException {
        technicalConditionService.changeTechnicalConditionStatus(technicalConditionId,status);
        return ResponseBean.success();
    }

    /**
     * 添加技术状态
     * @param technicalConditionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加技术状态失败", operation = "添加技术状态")
    @ApiOperation(value = "添加技术状态", notes = "添加技术状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:add"})
    @PostMapping("/addTechnicalCondition")
    public ResponseBean addTechnicalCondition(@RequestBody @Validated TechnicalConditionDTO technicalConditionDTO) throws AssetException {
        technicalConditionService.addTechnicalCondition(technicalConditionDTO);
        return ResponseBean.success();
    }

    /**
     * 修改技术状态
     * @param technicalConditionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改技术状态失败", operation = "修改技术状态")
    @ApiOperation(value = "修改技术状态", notes = "修改技术状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:edit"})
    @PutMapping("/updateTechnicalCondition")
    public ResponseBean updateTechnicalCondition(@RequestBody @Validated TechnicalConditionDTO technicalConditionDTO) throws AssetException {
        technicalConditionService.updateTechnicalCondition(technicalConditionDTO);
        return ResponseBean.success();
    }

    /**
     * 删除技术状态
     * @param technicalConditionId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除技术状态失败", operation = "删除技术状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:delete"})
    @ApiOperation(value = "删除技术状态", notes = "删除技术状态信息，根据技术状态ID")
    @DeleteMapping("/deleteTechnicalCondition/{technicalConditionId}")
    public ResponseBean deleteTechnicalCondition(@PathVariable Long technicalConditionId) throws AssetException {
        technicalConditionService.deleteTechnicalConditionById(technicalConditionId);
        return ResponseBean.success();
    }

    /**
     * 批量删除技术状态
     * @param technicalConditionIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除技术状态失败", operation = "批量删除技术状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:delete"})
    @ApiOperation(value = "批量删除技术状态", notes = "批量删除技术状态信息，根据技术状态ID")
    @DeleteMapping("/deleteTechnicalConditionByBatchId")
    public ResponseBean deleteTechnicalConditionByBatchId(@RequestBody List<Long> technicalConditionIds) throws AssetException {
        technicalConditionService.deleteTechnicalConditionByBatchId(technicalConditionIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入技术状态
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入技术状态失败", operation = "上传导入技术状态")
    @RequiresPermissions({"asset:metadata:technicalCondition:import"})
    @ApiOperation(value = "上传导入技术状态", notes = "上传导入技术状态")
    @PostMapping("/importTechnicalCondition")
    public ResponseBean importTechnicalCondition(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        technicalConditionService.importTechnicalCondition(fileMap);
        return ResponseBean.success();
    }
}
