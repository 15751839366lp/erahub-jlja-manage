package com.erahub.common.excel.model.fixedasset.metadata;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/27 15:36
 * @Version 1.0
 **/
@Data
public class DepreciationMethodExcel {

    @ColumnWidth(20)
    @ExcelProperty(value = "方法编号")
    private Long depreciationMethodId;

    @ColumnWidth(30)
    @ExcelProperty(value = "方法名称")
    private String depreciationMethodName;

    @ColumnWidth(20)
    @ExcelProperty(value = "资产数量")
    private Long fixedAssetQuantity;

    @ColumnWidth(20)
    @ExcelProperty(value = "类别数量")
    private Long fixedAssetCategoryQuantity;

    @ColumnWidth(20)
    @ExcelProperty(value = "状态")
    private Long status;

    @ColumnWidth(100)
    @ExcelProperty(value = "计算公式")
    private String formula;

    @ColumnWidth(100)
    @ExcelProperty(value = "公式说明")
    private String formulaExplain;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date createTime;

    @ColumnWidth(30)
    @ExcelProperty(value = "修改时间")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date modifiedTime;

    @ColumnWidth(100)
    @ExcelProperty(value = "备注")
    private String remark;
}
