package com.erahub.common.excel.model.asset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 9:23
 * @Version 1.0
 **/
@Data
public class ReduceReasonExcel {

    @ColumnWidth(10)
    @ExcelProperty(value = "减少原因编号")
    private Long reduceReasonId;

    @ColumnWidth(20)
    @ExcelProperty(value = "减少原因名称")
    private String reduceReasonName;

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
