package com.erahub.common.model.fixedasset.metadata;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产类别
 * @Author lipeng
 * @Date 2020/3/15 18:40
 * @Version 1.0
 **/
@Data
@TableName("fixed_asset_category")
public class FixedAssetCategory implements Serializable {

    @TableId
    private Long categoryId;

    private String categoryName;

    private Long categoryLevel;

    private Long categoryDetailed;

    private Long status;

    private Long depreciationMethodId;

    @TableField(exist = false)
    private String depreciationMethodName;

    private String measureUnit;

    private String capacityUnit;

    private Long depreciationPeriod;

    private Long estimatedTotalWorkload;

    private Long netResidualValue;

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
