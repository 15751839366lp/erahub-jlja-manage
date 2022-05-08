package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 9:22
 * @Version 1.0
 **/
@Data
public class ReduceMethodExcel {

    @ColumnWidth(10)
    @ExcelProperty(value = "减少方式编号")
    private Long reduceMethodId;

    @ColumnWidth(20)
    @ExcelProperty(value = "减少方式名称")
    private String reduceMethodName;

    @ColumnWidth(20)
    @ExcelProperty(value = "方式标记")
    private String methodMark;

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
