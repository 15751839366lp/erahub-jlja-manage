package com.erahub.controller.fixedasset.metadata;

import com.alibaba.excel.EasyExcel;
import com.erahub.common.annotation.ControllerEndpoint;
import com.erahub.common.dto.fixedasset.metadata.FixedAssetCategoryDTO;
import com.erahub.common.dto.fixedasset.metadata.SectionDTO;
import com.erahub.common.error.fixedasset.FixedAssetException;
import com.erahub.common.excel.model.fixedasset.metadata.FixedAssetCategoryExcel;
import com.erahub.common.excel.model.fixedasset.metadata.SectionExcel;
import com.erahub.common.response.ResponseBean;
import com.erahub.common.vo.common.PageVO;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import com.erahub.common.vo.fixedasset.metadata.SectionVO;
import com.erahub.fixedasset.metadata.service.SectionService;
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
 * @Date 2022/4/28 18:13
 * @Version 1.0
 **/
@RestController
@RequestMapping("/fixedasset/metadata/section")
@Validated
@Api(tags = "固定资产管理模块-元数据管理-单位相关接口")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /**
     * 获取单位列表
     * @param sectionDTO
     * @return
     */
    @ApiOperation(value = "单位列表", notes = "获取单位列表")
    @RequiresPermissions({"fixedAsset:metadata:section:select"})
    @PostMapping("/getSectionList")
    public ResponseBean<PageVO<SectionVO>> getSectionList(@RequestBody SectionDTO sectionDTO) {
        PageVO<SectionVO> resultData = sectionService.getSectionList(sectionDTO);
        return ResponseBean.success(resultData);
    }

    /**
     * 获取单位列表子节点
     * @param sectionId
     * @return
     */
    @ApiOperation(value = "单位列表子节点", notes = "获取单位列表子节点")
    @RequiresPermissions({"fixedAsset:metadata:section:select"})
    @GetMapping("/getChildrenList/{sectionId}")
    public ResponseBean<PageVO<SectionVO>> getChildrenList(@PathVariable String sectionId) throws FixedAssetException{
        PageVO<SectionVO> resultData = sectionService.getChildrenList(sectionId);
        return ResponseBean.success(resultData);
    }

    /**
     * 导出excel
     * @param response
     */
    @ApiOperation(value = "导出excel", notes = "导出所有使用单位的excel表格")
    @GetMapping("/exportSectionExcel")
    @RequiresPermissions("fixedAsset:metadata:section:export")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败",operation = "导出使用单位excel")
    public void exportSectionExcel(HttpServletResponse response) throws IOException {
        List<SectionExcel> sectionExcels = sectionService.exportSectionExcel();

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''使用单位列表.xlsx");
        EasyExcel.write(response.getOutputStream(), SectionExcel.class).sheet("使用单位列表").doWrite(sectionExcels);
    }

    /**
     * 更新使用单位状态
     * @param sectionId
     * @param status
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "更新使用单位状态失败", operation = "使用单位|禁用/启用")
    @ApiOperation(value = "使用单位", notes = "禁用和启用这两种状态")
    @RequiresPermissions({"fixedAsset:metadata:section:edit"})
    @PutMapping("/changeSectionStatus/{sectionId}/{status}")
    public ResponseBean changeSectionStatus(@PathVariable String sectionId, @PathVariable Long status) throws FixedAssetException {
        sectionService.changeSectionStatus(sectionId,status);
        return ResponseBean.success();
    }

    /**
     * 添加使用单位
     * @param sectionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "添加使用单位失败", operation = "添加使用单位")
    @ApiOperation(value = "添加使用单位", notes = "添加资使用单位")
    @RequiresPermissions({"fixedAsset:metadata:section:add"})
    @PostMapping("/addSection")
    public ResponseBean addSection(@RequestBody @Validated SectionDTO sectionDTO) throws FixedAssetException {
        sectionService.addSection(sectionDTO);
        return ResponseBean.success();
    }

    /**
     * 修改使用单位
     * @param sectionDTO
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "修改使用单位失败", operation = "修改使用单位")
    @ApiOperation(value = "修改使用单位", notes = "修改使用单位")
    @RequiresPermissions({"fixedAsset:metadata:section:edit"})
    @PutMapping("/updateSection")
    public ResponseBean updateSection(@RequestBody @Validated SectionDTO sectionDTO) throws FixedAssetException {
        sectionService.updateSection(sectionDTO);
        return ResponseBean.success();
    }

    /**
     * 删除使用单位
     * @param id
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "删除使用单位失败", operation = "删除使用单位")
    @RequiresPermissions({"fixedAsset:metadata:section:delete"})
    @ApiOperation(value = "删除使用单位", notes = "删除使用单位信息，根据使用单位ID")
    @DeleteMapping("/deleteSection/{id}")
    public ResponseBean deleteSection(@PathVariable String id) throws FixedAssetException {
        sectionService.deleteSectionById(id);
        return ResponseBean.success();
    }

    /**
     * 批量删除资产类别
     * @param sectionIds
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "批量删除使用单位失败", operation = "批量删除使用单位")
    @RequiresPermissions({"fixedAsset:metadata:section:delete"})
    @ApiOperation(value = "批量删除使用单位", notes = "批量删除使用单位信息，根据使用单位ID")
    @DeleteMapping("/deleteSectionByBatchId")
    public ResponseBean deleteSectionByBatchId(@RequestBody List<String> sectionIds) throws FixedAssetException {
        sectionService.deleteSectionByBatchId(sectionIds);
        return ResponseBean.success();
    }

    /**
     * 上传导入资产类别
     * @param request
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "上传导入使用单位失败", operation = "上传导入使用单位")
    @RequiresPermissions({"fixedAsset:metadata:section:import"})
    @ApiOperation(value = "上传导入使用单位", notes = "上传导入使用单位")
    @PostMapping("/importSection")
    public ResponseBean importSection(HttpServletRequest request) throws FixedAssetException, IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        sectionService.importSection(fileMap);
        return ResponseBean.success();
    }
}
