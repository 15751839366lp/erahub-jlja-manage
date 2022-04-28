package com.erahub.common.vo.fixedasset.metadata;


import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long sectionLevel;

    private Long sectionDetailed;

    private Long status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;

    private List<SectionVO> children;
}
