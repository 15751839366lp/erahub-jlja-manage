package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.ReduceReasonService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.ReduceReasonDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceReasonExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.ReduceReasonVO;
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
@RequestMapping("/asset/metadata/reducereason")
public class ReduceReasonController {

    @Autowired
    private ReduceReasonService reduceReasonService;

    /**
     * 查询减少原因列表
     * @param reduceReasonDTO
     * @return
     */
    @ApiOperation(value = "减少原因列表", notes = "查询减少原因列表")
    @RequiresPermissions({"asset:metadata:reduceReason:select"})
    @PostMapping("/getReduceReasonList")
    public ResponseBean<PageVO<ReduceReasonVO>> getReduceReasonList(@RequestBody ReduceReasonDTO reduceReasonDTO) {
        PageVO<ReduceReasonVO> resultData = reduceReasonService.getReduceReasonList(reduceReasonDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有减少原因的excel表格")
    @GetMapping("/exportReduceReasonExcel")
    @RequiresPermissions("asset:metadata:reduceReason:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出减少原因excel")
    public void exportReduceReasonExcel(HttpServletResponse response) throws IOException {
        List<ReduceReasonExcel> reduceReasonExcels = reduceReasonService.exportReduceReasonExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''减少原因列表.xlsx");
        EasyExcel.write(response.getOutputStream(), ReduceReasonExcel.class).sheet("减少原因列表").doWrite(reduceReasonExcels);
    }

    /**
     * 更新减少原因状态
     * @param reduceReasonId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新减少原因状态失败", operation = "减少原因|禁用/启用")
    @ApiOperation(value = "减少原因", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:reduceReason:edit"})
    @PutMapping("/changeReduceReasonStatus/{reduceReasonId}/{status}")
    public ResponseBean changeReduceReasonStatus(@PathVariable Long reduceReasonId, @PathVariable Integer status) throws AssetException {
        reduceReasonService.changeReduceReasonStatus(reduceReasonId,status);
        return ResponseBean.success();
    }

    /**
     * 添加减少原因
     * @param reduceReasonDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加减少原因失败", operation = "添加减少原因")
    @ApiOperation(value = "添加减少原因", notes = "添加减少原因")
    @RequiresPermissions({"asset:metadata:reduceReason:add"})
    @PostMapping("/addReduceReason")
    public ResponseBean addReduceReason(@RequestBody @Validated ReduceReasonDTO reduceReasonDTO) throws AssetException {
        reduceReasonService.addReduceReason(reduceReasonDTO);
        return ResponseBean.success();
    }

    /**
     * 修改减少原因
     * @param reduceReasonDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改减少原因失败", operation = "修改减少原因")
    @ApiOperation(value = "修改减少原因", notes = "修改减少原因")
    @RequiresPermissions({"asset:metadata:reduceReason:edit"})
    @PutMapping("/updateReduceReason")
    public ResponseBean updateReduceReason(@RequestBody @Validated ReduceReasonDTO reduceReasonDTO) throws AssetException {
        reduceReasonService.updateReduceReason(reduceReasonDTO);
        return ResponseBean.success();
    }

    /**
     * 删除减少原因
     * @param reduceReasonId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除减少原因失败", operation = "删除减少原因")
    @RequiresPermissions({"asset:metadata:reduceReason:delete"})
    @ApiOperation(value = "删除减少原因", notes = "删除减少原因信息，根据减少原因ID")
    @DeleteMapping("/deleteReduceReason/{reduceReasonId}")
    public ResponseBean deleteReduceReason(@PathVariable Long reduceReasonId) throws AssetException {
        reduceReasonService.deleteReduceReasonById(reduceReasonId);
        return ResponseBean.success();
    }

    /**
     * 批量删除减少原因
     * @param reduceReasonIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除减少原因失败", operation = "批量删除减少原因")
    @RequiresPermissions({"asset:metadata:reduceReason:delete"})
    @ApiOperation(value = "批量删除减少原因", notes = "批量删除减少原因信息，根据减少原因ID")
    @DeleteMapping("/deleteReduceReasonByBatchId")
    public ResponseBean deleteReduceReasonByBatchId(@RequestBody List<Long> reduceReasonIds) throws AssetException {
        reduceReasonService.deleteReduceReasonByBatchId(reduceReasonIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入减少原因
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入减少原因失败", operation = "上传导入减少原因")
    @RequiresPermissions({"asset:metadata:reduceReason:import"})
    @ApiOperation(value = "上传导入减少原因", notes = "上传导入减少原因")
    @PostMapping("/importReduceReason")
    public ResponseBean importReduceReason(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        reduceReasonService.importReduceReason(fileMap);
        return ResponseBean.success();
    }
}
