package com.erahub.common.vo.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 9:29
 * @Version 1.0
 **/
@Data
public class AssetSourceVO {

    private Long assetSourceId;

    private String assetSourceName;

    private String sourceMark;

    private Long assetQuantity;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
