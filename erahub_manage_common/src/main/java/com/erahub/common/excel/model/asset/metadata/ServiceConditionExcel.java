package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:07
 * @Version 1.0
 **/
@Data
public class ServiceConditionExcel {

    @ColumnWidth(10)
    @ExcelProperty(value = "使用状态编号")
    private Long serviceConditionId;

    @ColumnWidth(20)
    @ExcelProperty(value = "使用状态名称")
    private String serviceConditionName;

    @ColumnWidth(20)
    @ExcelProperty(value = "资产数量")
    private Long assetQuantity;

    @ColumnWidth(10)
    @ExcelProperty(value = "是否计提折旧")
    private Long accrualDepreciation;

    @ColumnWidth(10)
    @ExcelProperty(value = "状态")
    private Long status;

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
