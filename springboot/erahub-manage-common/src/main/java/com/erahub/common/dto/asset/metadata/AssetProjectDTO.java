package com.erahub.common.dto.asset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/9 18:56
 * @Version 1.0
 **/
@Data
public class AssetProjectDTO {

    private String assetProjectId;

    private String assetProjectName;

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
