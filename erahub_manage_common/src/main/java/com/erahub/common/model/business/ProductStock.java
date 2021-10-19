package com.erahub.common.model.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品库存
 */
@Data
@TableName("biz_product_stock")
public class ProductStock {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String pNum;

    private Long stock;

}
