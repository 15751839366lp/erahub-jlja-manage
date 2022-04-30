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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
}
