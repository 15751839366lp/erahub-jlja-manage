package com.erahub.common.model.business.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_supplier")
public class Supplier {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String phone;

    private Date createTime;

    private Date modifiedTime;

    private Integer sort;

    private String contact;

}
