package com.erahub.common.model.business.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_product_category")
public class ProductCategory {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String remark;

    private Integer sort;

    private Date createTime;

    private Date modifiedTime;

    private Long pid;

}
