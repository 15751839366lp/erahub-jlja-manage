package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/21 15:13
 * @Version 1.0
 **/
@Data
public class AssetCategoryExcel {

    @ColumnWidth(20)
    @ExcelProperty(value = "类别编号")
    private String assetCategoryId;

    @ColumnWidth(40)
    @ExcelProperty(value = "类别名称")
    private String assetCategoryName;

    @ColumnWidth(20)
    @ExcelProperty(value = "资产数量")
    private Long assetQuantity;

    @ColumnWidth(10)
    @ExcelProperty(value = "类别等级")
    private Long level;

    @ColumnWidth(10)
    @ExcelProperty(value = "明细")
    private Long detailed;

    @ColumnWidth(10)
    @ExcelProperty(value = "状态")
    private Long status;

    @ColumnWidth(10)
    @ExcelProperty(value = "折旧方法id")
    private Long depreciationMethodId;

    @ColumnWidth(40)
    @ExcelProperty(value = "折旧方法")
    private String depreciationMethodName;

    @ColumnWidth(10)
    @ExcelProperty(value = "计量单位")
    private String measureUnit;

    @ColumnWidth(10)
    @ExcelProperty(value = "能力单位")
    private String capacityUnit;

    @ColumnWidth(10)
    @ExcelProperty(value = "折旧年限")
    private BigDecimal depreciationPeriod;

    @ColumnWidth(10)
    @ExcelProperty(value = "预计总工作量")
    private BigDecimal estimatedTotalWorkload;

    @ColumnWidth(10)
    @ExcelProperty(value = "净残值率")
    private BigDecimal netResidualValue;

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
