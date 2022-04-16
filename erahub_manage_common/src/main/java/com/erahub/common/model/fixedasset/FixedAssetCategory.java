package com.erahub.common.model.fixedasset;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
@TableName("fixed_asset_category")
public class FixedAssetCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long category_id;

    private String category_name;

    private Long category_level;

    private Long category_detailed;

    private Long status;

    private Long depreciation_method_id;

    private String measure_unit;

    private String capacity_unit;

    private Long depreciation_period;

    private Long estimated_total_workload;

    private Long net_residual_value;

    private Date create_time;

    private Date modified_time;

    private String remark;

}
