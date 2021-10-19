package com.erahub.common.model.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_out_stock")
public class OutStock {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String outNum;

    private Integer type;

    private String operator;

    private Date createTime;

    private Integer productNumber;

    private Long consumerId;

    private String remark;

    private Integer status;

    private Integer priority;

}
