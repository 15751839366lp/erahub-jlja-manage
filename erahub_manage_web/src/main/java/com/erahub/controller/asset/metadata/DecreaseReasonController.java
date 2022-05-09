package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.DecreaseReasonService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.DecreaseReasonDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseReasonExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.DecreaseReasonVO;
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
@Api(tags = "资产管理模块-元数据管理-减少原因相关接口")
@RequestMapping("/asset/metadata/decreasereason")
public class DecreaseReasonController {

    @Autowired
    private DecreaseReasonService decreaseReasonService;

    /**
     * 查询减少原因列表
     * @param decreaseReasonDTO
     * @return
     */
    @ApiOperation(value = "减少原因列表", notes = "查询减少原因列表")
    @RequiresPermissions({"asset:metadata:decreaseReason:select"})
    @PostMapping("/getDecreaseReasonList")
    public ResponseBean<PageVO<DecreaseReasonVO>> getDecreaseReasonList(@RequestBody DecreaseReasonDTO decreaseReasonDTO) {
        PageVO<DecreaseReasonVO> resultData = decreaseReasonService.getDecreaseReasonList(decreaseReasonDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有减少原因的excel表格")
    @GetMapping("/exportDecreaseReasonExcel")
    @RequiresPermissions("asset:metadata:decreaseReason:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出减少原因excel")
    public void exportDecreaseReasonExcel(HttpServletResponse response) throws IOException {
        List<DecreaseReasonExcel> decreaseReasonExcels = decreaseReasonService.exportDecreaseReasonExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''减少原因列表.xlsx");
        EasyExcel.write(response.getOutputStream(), DecreaseReasonExcel.class).sheet("减少原因列表").doWrite(decreaseReasonExcels);
    }

    /**
     * 更新减少原因状态
     * @param decreaseReasonId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新减少原因状态失败", operation = "减少原因|禁用/启用")
    @ApiOperation(value = "减少原因", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:decreaseReason:edit"})
    @PutMapping("/changeDecreaseReasonStatus/{decreaseReasonId}/{status}")
    public ResponseBean changeDecreaseReasonStatus(@PathVariable Long decreaseReasonId, @PathVariable Integer status) throws AssetException {
        decreaseReasonService.changeDecreaseReasonStatus(decreaseReasonId,status);
        return ResponseBean.success();
    }

    /**
     * 添加减少原因
     * @param decreaseReasonDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加减少原因失败", operation = "添加减少原因")
    @ApiOperation(value = "添加减少原因", notes = "添加减少原因")
    @RequiresPermissions({"asset:metadata:decreaseReason:add"})
    @PostMapping("/addDecreaseReason")
    public ResponseBean addDecreaseReason(@RequestBody @Validated DecreaseReasonDTO decreaseReasonDTO) throws AssetException {
        decreaseReasonService.addDecreaseReason(decreaseReasonDTO);
        return ResponseBean.success();
    }

    /**
     * 修改减少原因
     * @param decreaseReasonDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改减少原因失败", operation = "修改减少原因")
    @ApiOperation(value = "修改减少原因", notes = "修改减少原因")
    @RequiresPermissions({"asset:metadata:decreaseReason:edit"})
    @PutMapping("/updateDecreaseReason")
    public ResponseBean updateDecreaseReason(@RequestBody @Validated DecreaseReasonDTO decreaseReasonDTO) throws AssetException {
        decreaseReasonService.updateDecreaseReason(decreaseReasonDTO);
        return ResponseBean.success();
    }

    /**
     * 删除减少原因
     * @param decreaseReasonId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除减少原因失败", operation = "删除减少原因")
    @RequiresPermissions({"asset:metadata:decreaseReason:delete"})
    @ApiOperation(value = "删除减少原因", notes = "删除减少原因信息，根据减少原因ID")
    @DeleteMapping("/deleteDecreaseReason/{decreaseReasonId}")
    public ResponseBean deleteDecreaseReason(@PathVariable Long decreaseReasonId) throws AssetException {
        decreaseReasonService.deleteDecreaseReasonById(decreaseReasonId);
        return ResponseBean.success();
    }

    /**
     * 批量删除减少原因
     * @param decreaseReasonIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除减少原因失败", operation = "批量删除减少原因")
    @RequiresPermissions({"asset:metadata:decreaseReason:delete"})
    @ApiOperation(value = "批量删除减少原因", notes = "批量删除减少原因信息，根据减少原因ID")
    @DeleteMapping("/deleteDecreaseReasonByBatchId")
    public ResponseBean deleteDecreaseReasonByBatchId(@RequestBody List<Long> decreaseReasonIds) throws AssetException {
        decreaseReasonService.deleteDecreaseReasonByBatchId(decreaseReasonIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入减少原因
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入减少原因失败", operation = "上传导入减少原因")
    @RequiresPermissions({"asset:metadata:decreaseReason:import"})
    @ApiOperation(value = "上传导入减少原因", notes = "上传导入减少原因")
    @PostMapping("/importDecreaseReason")
    public ResponseBean importDecreaseReason(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        decreaseReasonService.importDecreaseReason(fileMap);
        return ResponseBean.success();
    }
}
