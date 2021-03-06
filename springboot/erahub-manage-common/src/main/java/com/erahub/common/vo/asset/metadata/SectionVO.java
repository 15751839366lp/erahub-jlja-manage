package com.erahub.common.vo.asset.metadata;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author lipeng
 * @Date 2022/4/28 17:59
 * @Version 1.0
 **/
@Data
public class SectionVO {

    private String sectionId;

    private String sectionName;

    private String sectionAbbreviation;

    private Long assetQuantity;

    private Integer level;

    private Integer detailed;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private List<SectionVO> children;

    private Boolean hasChildren;
}
