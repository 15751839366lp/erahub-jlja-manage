package com.erahub.controller.fixedasset.metadata;

import com.alibaba.excel.EasyExcel;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.fixedasset.metadata.DepreciationMethodDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.DepreciationMethodExcel;
import com.erahub.common.model.fixedasset.metadata.DepreciationMethod;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.DepreciationMethodVO;
import com.erahub.fixedasset.metadata.service.DepreciationMethodService;
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
 * @Date 2022/4/21 19:26
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fixedasset/metadata/depreciationmethod")
@Validated
@Api(tags = "固定资产管理模块-元数据管理-折旧方法相关接口")
public class DepreciationMethodController {

    @Autowired
    DepreciationMethodService depreciationMethodService;

    /**
     * 所有折旧方法
     * @return
     */
    @ApiOperation(value = "所有折旧方法")
    @GetMapping("/getAllDepreciationMethod")
    public ResponseBean<List<DepreciationMethod>> getAllDepreciationMethod() {
        List<DepreciationMethod> depreciationMethods = depreciationMethodService.list();
        return ResponseBean.success(depreciationMethods);
    }

    /**
     * 查询折旧方法列表
     * @return
     */
    @ApiOperation(value = "折旧方法列表", notes = "查询折旧方法列表")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:select"})
    @PostMapping("/getDepreciationMethodList")
    public ResponseBean<PageVO<DepreciationMethodVO>> getDepreciationMethodList(@RequestBody DepreciationMethodDTO depreciationMethodDTO) {
        PageVO<DepreciationMethodVO> resultData = depreciationMethodService.getDepreciationMethodList(depreciationMethodDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有折旧方法的excel表格")
    @GetMapping("/exportDepreciationMethodExcel")
    @RequiresPermissions("fixedAsset:metadata:depreciationMethod:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出折旧方法excel")
    public void exportDepreciationMethodExcel(HttpServletResponse response) throws IOException {
        List<DepreciationMethodExcel> depreciationMethodExcels = depreciationMethodService.exportDepreciationMethodExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''折旧方法列表.xlsx");
        EasyExcel.write(response.getOutputStream(), DepreciationMethodExcel.class).sheet("折旧方法列表").doWrite(depreciationMethodExcels);
    }

    /**
     * 更新折旧方法状态
     * @param depreciationMethodId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新折旧方法状态失败", operation = "折旧方法|禁用/启用")
    @ApiOperation(value = "固定资产折旧方法", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:edit"})
    @PutMapping("/changeDepreciationMethodStatus/{depreciationMethodId}/{status}")
    public ResponseBean changeDepreciationMethodStatus(@PathVariable Long depreciationMethodId, @PathVariable Long status) throws FixedAssetException {
        depreciationMethodService.changeDepreciationMethodStatus(depreciationMethodId,status);
        return ResponseBean.success();
    }

    /**
     * 添加折旧方法
     * @param depreciationMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加折旧方法失败", operation = "添加折旧方法")
    @ApiOperation(value = "添加折旧方法", notes = "添加折旧方法")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:add"})
    @PostMapping("/addDepreciationMethod")
    public ResponseBean addDepreciationMethod(@RequestBody @Validated DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException {
        depreciationMethodService.addDepreciationMethod(depreciationMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 修改折旧方法
     * @param depreciationMethodDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改折旧方法失败", operation = "修改折旧方法")
    @ApiOperation(value = "修改折旧方法", notes = "修改折旧方法")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:edit"})
    @PutMapping("/updateDepreciationMethod")
    public ResponseBean updateDepreciationMethod(@RequestBody @Validated DepreciationMethodDTO depreciationMethodDTO) throws FixedAssetException {
        depreciationMethodService.updateDepreciationMethod(depreciationMethodDTO);
        return ResponseBean.success();
    }

    /**
     * 删除折旧方法
     * @param id
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除折旧方法失败", operation = "删除折旧方法")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:delete"})
    @ApiOperation(value = "删除折旧方法", notes = "删除折旧方法信息，根据折旧方法ID")
    @DeleteMapping("/deleteDepreciationMethod/{id}")
    public ResponseBean deleteDepreciationMethod(@PathVariable Long id) throws FixedAssetException {
        depreciationMethodService.deleteDepreciationMethod(id);
        return ResponseBean.success();
    }

    /**
     * 批量删除折旧方法
     * @param depreciationMethodIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除折旧方法失败", operation = "批量删除折旧方法")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:delete"})
    @ApiOperation(value = "批量删除折旧方法", notes = "批量删除折旧方法信息，根据折旧方法ID")
    @DeleteMapping("/deleteDepreciationMethodByBatchId")
    public ResponseBean deleteDepreciationMethodByBatchId(@RequestBody List<Long> depreciationMethodIds) throws FixedAssetException {
        depreciationMethodService.deleteDepreciationMethodByBatchId(depreciationMethodIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入折旧方法
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入折旧方法失败", operation = "上传导入折旧方法")
    @RequiresPermissions({"fixedAsset:metadata:depreciationMethod:import"})
    @ApiOperation(value = "上传导入折旧方法", notes = "上传导入折旧方法")
    @PostMapping("/importDepreciationMethod")
    public ResponseBean importDepreciationMethod(HttpServletRequest request) throws FixedAssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        depreciationMethodService.importDepreciationMethod(fileMap);
        return ResponseBean.success();
    }
}
