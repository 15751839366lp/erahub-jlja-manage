package com.erahub.common.model.business.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_in_stock")
public class InStock {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String inNum;

    private Integer type;

    private String operator;

    private Long supplierId;

    private Date createTime;

    private Date modified;

    private Integer productNumber;

    private String remark;

    private Integer status;
}
