package com.erahub.common.vo.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 9:30
 * @Version 1.0
 **/
@Data
public class TechnicalConditionVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long technicalConditionId;

    private String technicalConditionName;

    private Long assetQuantity;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
