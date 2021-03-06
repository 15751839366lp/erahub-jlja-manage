package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/28 17:54
 * @Version 1.0
 **/
@Data
public class SectionExcel {


    @ColumnWidth(20)
    @ExcelProperty(value = "单位编号")
    private String sectionId;

    @ColumnWidth(40)
    @ExcelProperty(value = "单位名称")
    private String sectionName;

    @ColumnWidth(40)
    @ExcelProperty(value = "单位简称")
    private String sectionAbbreviation;

    @ColumnWidth(20)
    @ExcelProperty(value = "资产数量")
    private Long assetQuantity;

    @ColumnWidth(10)
    @ExcelProperty(value = "单位等级")
    private Integer level;

    @ColumnWidth(10)
    @ExcelProperty(value = "明细")
    private Integer detailed;

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

    @ExcelProperty(value = "备注")
    private String remark;
}
