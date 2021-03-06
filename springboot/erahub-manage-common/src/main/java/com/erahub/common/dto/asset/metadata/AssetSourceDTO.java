package com.erahub.common.dto.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/8 8:58
 * @Version 1.0
 **/
@Data
public class AssetSourceDTO {

    private Long assetSourceId;

    private String assetSourceName;

    private String sourceMark;

    private Long assetQuantity;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private Integer pageNum;

    private Integer pageSize;

    private Integer isAccurate;

    private String sortColumn;

    private Boolean isAsc;

}
