package com.erahub.common.dto.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/17 10:51
 * @Version 1.0
 **/
@Data
public class AssetCategoryDTO {

    private String assetCategoryId;

    private String assetCategoryName;

    private Integer level;

    private Long assetQuantity;

    private Integer detailed;

    private Integer status;

    private Long depreciationMethodId;

    private String measureUnit;

    private String capacityUnit;

    private BigDecimal depreciationPeriod;

    private BigDecimal estimatedTotalWorkload;

    private BigDecimal netResidualValue;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private Integer pageNum;

    private Integer pageSize;

    private Integer isAccurate;

    private String sortColumn;

    private Boolean isAsc;

}
