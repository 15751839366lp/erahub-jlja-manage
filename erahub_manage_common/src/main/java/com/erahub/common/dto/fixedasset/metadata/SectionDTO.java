package com.erahub.common.dto.fixedasset.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/28 17:51
 * @Version 1.0
 **/
@Data
public class SectionDTO {

    private String sectionId;

    private String sectionName;

    private String sectionAbbreviation;

    private Long fixedAssetQuantity;

    private Long sectionLevel;

    private Long sectionDetailed;

    private Long status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private String parentSectionId;

    private Integer pageNum;

    private Integer pageSize;

    private Integer isAccurate;
}
