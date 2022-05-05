package com.erahub.common.vo.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 资产类别
 * @Author lipeng
 * @Date 2020/3/15 18:40
 * @Version 1.0
 **/
@Data
public class AssetCategoryVO {

    private String assetCategoryId;

    private String assetCategoryName;

    private Long assetQuantity;

    private Long level;

    private Long detailed;

    private Long status;

    private Long depreciationMethodId;

    private String depreciationMethodName;

    private String measureUnit;

    private String capacityUnit;

    private BigDecimal depreciationPeriod;

    private BigDecimal estimatedTotalWorkload;

    private BigDecimal netResidualValue;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private List<AssetCategoryVO> children;
}
