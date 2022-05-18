package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/9 18:59
 * @Version 1.0
 **/
@Data
public class AssetProjectExcel {

    @ColumnWidth(10)
    @ExcelProperty(value = "工程项目编号")
    private String assetProjectId;

    @ColumnWidth(20)
    @ExcelProperty(value = "工程项目名称")
    private String assetProjectName;

    @ColumnWidth(20)
    @ExcelProperty(value = "资产数量")
    private Long assetQuantity;

    @ColumnWidth(10)
    @ExcelProperty(value = "状态")
    private Integer status;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date createTime;

    @ColumnWidth(30)
    @ExcelProperty(value = "修改时间")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date modifiedTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "备注")
    private String remark;
}
