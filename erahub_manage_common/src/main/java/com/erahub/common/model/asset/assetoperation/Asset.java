package com.erahub.common.model.asset.assetoperation;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:37
 * @Version 1.0
 **/
@Data
@TableName("as_asset")
public class Asset {

    @TableId
    private String assetId;

    private String sectionId;

    private Long depreciationMethodId;

    private String assetCategoryId;

    private Long serviceConditionId;
}
