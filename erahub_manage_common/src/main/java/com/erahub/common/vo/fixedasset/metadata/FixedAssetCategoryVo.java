package com.erahub.common.vo.fixedasset.metadata;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 资产类别
 * @Author lipeng
 * @Date 2020/3/15 18:40
 * @Version 1.0
 **/
@Data
public class FixedAssetCategoryVo {

    private Long categoryId;

    private String categoryName;

    private Long categoryLevel;

    private Long categoryDetailed;

    private Long status;

    private Long depreciationMethodId;

    private String measureUnit;

    private String capacityUnit;

    private Long depreciationPeriod;

    private Long estimatedTotalWorkload;

    private Long netResidualValue;

    private Date createTime;

    private Date modifiedTime;

    private String remark;

    private List<FixedAssetCategoryVo> children;
}
