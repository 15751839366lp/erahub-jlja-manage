package com.erahub.common.vo.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/27 15:34
 * @Version 1.0
 **/
@Data
public class DepreciationMethodVO {

    private Long depreciationMethodId;

    private String depreciationMethodName;

    private Long assetQuantity;

    private Long assetCategoryQuantity;

    private String formula;

    private String formulaExplain;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
