package com.erahub.common.model.business.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@TableName("biz_consumer")
@Data
public class Consumer {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private Date createTime;

    private Date modifiedTime;

    private String phone;

    private  Integer sort;

    private String contact;
}
