package com.erahub.common.model.business.material;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("biz_product")
public class Product {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String pNum;

    private String name;

    private String model;

    private String unit;

    private String remark;

    private Integer sort;

    private Date createTime;

    private Date modifiedTime;

    private Long oneCategoryId;

    private Long twoCategoryId;

    private Long threeCategoryId;

    private String imageUrl;

    private Integer status;

}
