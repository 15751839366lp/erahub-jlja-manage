package com.erahub.common.model.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_health")
public class Health {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String address;

    private Long userId;

    private Integer situation;

    private Integer touch;

    private Integer passby;

    private Integer reception;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
