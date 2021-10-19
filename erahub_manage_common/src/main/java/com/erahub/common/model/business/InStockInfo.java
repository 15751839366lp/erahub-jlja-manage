package com.erahub.common.model.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_in_stock_info")
public class InStockInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String inNum;

    private String pNum;

    private Integer productNumber;

    private Date createTime;

    private Date modifiedTime;

}
