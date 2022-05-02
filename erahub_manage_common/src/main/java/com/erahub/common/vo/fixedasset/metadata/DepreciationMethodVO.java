package com.erahub.common.vo.fixedasset.metadata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    private Long fixedAssetQuantity;

    private Long fixedAssetCategoryQuantity;

    private String formula;

    private String formulaExplain;

    private Long status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
