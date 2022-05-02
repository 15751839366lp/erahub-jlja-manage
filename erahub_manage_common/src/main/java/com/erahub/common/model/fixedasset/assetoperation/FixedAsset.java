package com.erahub.common.model.fixedasset.assetoperation;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:37
 * @Version 1.0
 **/
@Data
@TableName("fa_fixed_asset")
public class FixedAsset {

    @TableId
    private String fixedAssetId;

    private String sectionId;

    private Long depreciationMethodId;

    private String categoryId;
}
