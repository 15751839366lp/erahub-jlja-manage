package com.erahub.common.vo.business.material;

import lombok.Data;

/** εεεΊε­
 * @Author lipeng
 * @Date 2020/4/16 09:27
 * @Version 1.0
 **/
@Data
public class ProductStockVO {

    private Long id;

    private String name;

    private String pNum;

    private String model;

    private String unit;

    private String remark;

    private Long stock;

    private String imageUrl;
}
