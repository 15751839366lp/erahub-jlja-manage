package com.erahub.common.vo.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/7 9:17
 * @Version 1.0
 **/
@Data
public class ServiceConditionVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long serviceConditionId;

    private String serviceConditionName;

    private Long assetQuantity;

    private Long accrualDepreciation;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
