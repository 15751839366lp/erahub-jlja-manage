package com.erahub.common.model.fixedasset.metadata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/4/28 17:56
 * @Version 1.0
 **/
@Data
@TableName("fa_section")
public class Section implements Serializable {

    private String sectionId;

    private String sectionName;

    private String sectionAbbreviation;

    private Long sectionLevel;

    private Long sectionDetailed;

    private Long status;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String remark;
}
