package com.erahub.common.model.asset.metadata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 减少方式
 * @Author lipeng
 * @Date 2022/4/21 17:58
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("as_decrease_method")
public class DecreaseMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 减少方式编号
     */
    @TableId
    private Long decreaseMethodId;

    /**
     * 减少方式名称
     */
    private String decreaseMethodName;

    /**
     * ‘0’一般 ‘3’ 盘亏 ‘5’调出 ‘9’评估
“调出”标记用以自动处理资产调拨，“盘亏”用于处理资产盘点时盘亏资产的自动判断，“评估”标记处理评估减值

     */
    private String methodMark;

    /**
     * 资产数量
     */
    @TableField(exist = false)
    private Long assetQuantity;

    /**
     * 是否使用    1 是  0 否
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 备注
     */
    private String remark;


}
