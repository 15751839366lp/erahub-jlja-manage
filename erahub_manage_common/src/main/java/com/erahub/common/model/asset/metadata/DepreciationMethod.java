package com.erahub.common.model.asset.metadata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 折旧方法
 * @Author lipeng
 * @Date 2022/4/21 17:58
 * @Version 1.0
 **/
@Data
@TableName("as_depreciation_method")
public class DepreciationMethod implements Serializable {

    @TableId
    private Long depreciationMethodId;

    private String depreciationMethodName;

    @TableField(exist = false)
    private Long assetQuantity;

    @TableField(exist = false)
    private Long assetCategoryQuantity;

    private String formula;

    private String formulaExplain;

    private Long status;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
