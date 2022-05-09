package com.erahub.controller.asset.metadata;


import com.alibaba.excel.EasyExcel;
import com.erahub.asset.metadata.service.DecreaseMethodService;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.asset.metadata.DecreaseMethodDTO;
import com.erahub.common.error.asset.AssetException;
import com.erahub.common.excel.model.asset.metadata.DecreaseMethodExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.asset.metadata.DecreaseMethodVO;
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
@RequestMapping("/asset/metadata/decreasemethod")
public class DecreaseMethodController {

    @Autowired
    private DecreaseMethodService decreaseMethodService;

    /**
     * 查询减少方式列表
     * @param decreaseMethodDTO
     * @return
     */
    @ApiOperation(value = "减少方式列表", notes = "查询减少方式列表")
    @RequiresPermissions({"asset:metadata:decreaseMethod:select"})
    @PostMapping("/getDecreaseMethodList")
    public ResponseBean<PageVO<DecreaseMethodVO>> getDecreaseMethodList(@RequestBody DecreaseMethodDTO decreaseMethodDTO) {
        PageVO<DecreaseMethodVO> resultData = decreaseMethodService.getDecreaseMethodList(decreaseMethodDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有减少方式的excel表格")
    @GetMapping("/exportDecreaseMethodExcel")
    @RequiresPermissions("asset:metadata:decreaseMethod:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出减少方式excel")
    public void exportDecreaseMethodExcel(HttpServletResponse response) throws IOException {
        List<DecreaseMethodExcel> decreaseMethodExcels = decreaseMethodService.exportDecreaseMethodExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''减少方式列表.xlsx");
        EasyExcel.write(response.getOutputStream(), DecreaseMethodExcel.class).sheet("减少方式列表").doWrite(decreaseMethodExcels);
    }

    /**
     * 更新减少方式状态
     * @param decreaseMethodId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新减少方式状态失败", operation = "减少方式|禁用/启用")
    @ApiOperation(value = "减少方式", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"asset:metadata:decreaseMethod:edit"})
    @PutMapping("/changeDecreaseMethodStatus/{decreaseMethodId}/{status}")
    public ResponseBean changeDecreaseMethodStatus(@PathVariable Long decreaseMethodId, @PathVariable Integer status) throws AssetException {
        decreaseMethodService.changeDecreaseMethodStatus(decreaseMethodId,status);
        return ResponseBean.success();
    }

    /**
     * 添加减少方式
     * @param decreaseMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加减少方式失败", operation = "添加减少方式")
    @ApiOperation(value = "添加减少方式", notes = "添加减少方式")
    @RequiresPermissions({"asset:metadata:decreaseMethod:add"})
    @PostMapping("/addDecreaseMethod")
    public ResponseBean addDecreaseMethod(@RequestBody @Validated DecreaseMethodDTO decreaseMethodDTO) throws AssetException {
        decreaseMethodService.addDecreaseMethod(decreaseMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 修改减少方式
     * @param decreaseMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改减少方式失败", operation = "修改减少方式")
    @ApiOperation(value = "修改减少方式", notes = "修改减少方式")
    @RequiresPermissions({"asset:metadata:decreaseMethod:edit"})
    @PutMapping("/updateDecreaseMethod")
    public ResponseBean updateDecreaseMethod(@RequestBody @Validated DecreaseMethodDTO decreaseMethodDTO) throws AssetException {
        decreaseMethodService.updateDecreaseMethod(decreaseMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 删除减少方式
     * @param decreaseMethodId
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除减少方式失败", operation = "删除减少方式")
    @RequiresPermissions({"asset:metadata:decreaseMethod:delete"})
    @ApiOperation(value = "删除减少方式", notes = "删除减少方式信息，根据减少方式ID")
    @DeleteMapping("/deleteDecreaseMethod/{decreaseMethodId}")
    public ResponseBean deleteDecreaseMethod(@PathVariable Long decreaseMethodId) throws AssetException {
        decreaseMethodService.deleteDecreaseMethodById(decreaseMethodId);
        return ResponseBean.success();
    }

    /**
     * 批量删除减少方式
     * @param decreaseMethodIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除减少方式失败", operation = "批量删除减少方式")
    @RequiresPermissions({"asset:metadata:decreaseMethod:delete"})
    @ApiOperation(value = "批量删除减少方式", notes = "批量删除减少方式信息，根据减少方式ID")
    @DeleteMapping("/deleteDecreaseMethodByBatchId")
    public ResponseBean deleteDecreaseMethodByBatchId(@RequestBody List<Long> decreaseMethodIds) throws AssetException {
        decreaseMethodService.deleteDecreaseMethodByBatchId(decreaseMethodIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入减少方式
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入减少方式失败", operation = "上传导入减少方式")
    @RequiresPermissions({"asset:metadata:decreaseMethod:import"})
    @ApiOperation(value = "上传导入减少方式", notes = "上传导入减少方式")
    @PostMapping("/importDecreaseMethod")
    public ResponseBean importDecreaseMethod(HttpServletRequest request) throws AssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        decreaseMethodService.importDecreaseMethod(fileMap);
        return ResponseBean.success();
    }
}
