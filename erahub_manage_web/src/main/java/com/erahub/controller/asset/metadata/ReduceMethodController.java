package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.ReduceMethodService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.ReduceMethodDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.ReduceMethodExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.ReduceMethodVO;
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
@Api(tags = "资产管理模块-元数据管理-减少方式相关接口")
@RequestMapping("/asset/metadata/reducemethod")
public class ReduceMethodController {

    @Autowired
    private ReduceMethodService reduceMethodService;

    /**
     * 查询减少方式列表
     * @param reduceMethodDTO
     * @return
     */
    @ApiOperation(value = "减少方式列表", notes = "查询减少方式列表")
    @RequiresPermissions({"asset:metadata:reduceMethod:select"})
    @PostMapping("/getReduceMethodList")
    public ResponseBean<PageVO<ReduceMethodVO>> getReduceMethodList(@RequestBody ReduceMethodDTO reduceMethodDTO) {
        PageVO<ReduceMethodVO> resultData = reduceMethodService.getReduceMethodList(reduceMethodDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有减少方式的excel表格")
    @GetMapping("/exportReduceMethodExcel")
    @RequiresPermissions("asset:metadata:reduceMethod:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出减少方式excel")
    public void exportReduceMethodExcel(HttpServletResponse response) throws IOException {
        List<ReduceMethodExcel> reduceMethodExcels = reduceMethodService.exportReduceMethodExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''减少方式列表.xlsx");
        EasyExcel.write(response.getOutputStream(), ReduceMethodExcel.class).sheet("减少方式列表").doWrite(reduceMethodExcels);
    }

    /**
     * 更新减少方式状态
     * @param reduceMethodId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新减少方式状态失败", operation = "减少方式|禁用/启用")
    @ApiOperation(value = "减少方式", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:reduceMethod:edit"})
    @PutMapping("/changeReduceMethodStatus/{reduceMethodId}/{status}")
    public ResponseBean changeReduceMethodStatus(@PathVariable Long reduceMethodId, @PathVariable Integer status) throws AssetException {
        reduceMethodService.changeReduceMethodStatus(reduceMethodId,status);
        return ResponseBean.success();
    }

    /**
     * 添加减少方式
     * @param reduceMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加减少方式失败", operation = "添加减少方式")
    @ApiOperation(value = "添加减少方式", notes = "添加减少方式")
    @RequiresPermissions({"asset:metadata:reduceMethod:add"})
    @PostMapping("/addReduceMethod")
    public ResponseBean addReduceMethod(@RequestBody @Validated ReduceMethodDTO reduceMethodDTO) throws AssetException {
        reduceMethodService.addReduceMethod(reduceMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 修改减少方式
     * @param reduceMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改减少方式失败", operation = "修改减少方式")
    @ApiOperation(value = "修改减少方式", notes = "修改减少方式")
    @RequiresPermissions({"asset:metadata:reduceMethod:edit"})
    @PutMapping("/updateReduceMethod")
    public ResponseBean updateReduceMethod(@RequestBody @Validated ReduceMethodDTO reduceMethodDTO) throws AssetException {
        reduceMethodService.updateReduceMethod(reduceMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 删除减少方式
     * @param reduceMethodId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除减少方式失败", operation = "删除减少方式")
    @RequiresPermissions({"asset:metadata:reduceMethod:delete"})
    @ApiOperation(value = "删除减少方式", notes = "删除减少方式信息，根据减少方式ID")
    @DeleteMapping("/deleteReduceMethod/{reduceMethodId}")
    public ResponseBean deleteReduceMethod(@PathVariable Long reduceMethodId) throws AssetException {
        reduceMethodService.deleteReduceMethodById(reduceMethodId);
        return ResponseBean.success();
    }

    /**
     * 批量删除减少方式
     * @param reduceMethodIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除减少方式失败", operation = "批量删除减少方式")
    @RequiresPermissions({"asset:metadata:reduceMethod:delete"})
    @ApiOperation(value = "批量删除减少方式", notes = "批量删除减少方式信息，根据减少方式ID")
    @DeleteMapping("/deleteReduceMethodByBatchId")
    public ResponseBean deleteReduceMethodByBatchId(@RequestBody List<Long> reduceMethodIds) throws AssetException {
        reduceMethodService.deleteReduceMethodByBatchId(reduceMethodIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入减少方式
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入减少方式失败", operation = "上传导入减少方式")
    @RequiresPermissions({"asset:metadata:reduceMethod:import"})
    @ApiOperation(value = "上传导入减少方式", notes = "上传导入减少方式")
    @PostMapping("/importReduceMethod")
    public ResponseBean importReduceMethod(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        reduceMethodService.importReduceMethod(fileMap);
        return ResponseBean.success();
    }
}
