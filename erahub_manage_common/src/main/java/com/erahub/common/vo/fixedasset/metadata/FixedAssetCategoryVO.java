package com.erahub.common.vo.fixedasset.metadata;

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
public class FixedAssetCategoryVO {

    private Long categoryId;

    private String categoryName;

    private Long categoryLevel;

    private Long categoryDetailed;

    private Boolean status;

    private Long depreciationMethodId;

    private String measureUnit;

    private String capacityUnit;

    private BigDecimal depreciationPeriod;

    private BigDecimal estimatedTotalWorkload;

    private BigDecimal netResidualValue;

    private Date createTime;

    private Date modifiedTime;

    private String remark;

    private List<FixedAssetCategoryVO> children;
}
