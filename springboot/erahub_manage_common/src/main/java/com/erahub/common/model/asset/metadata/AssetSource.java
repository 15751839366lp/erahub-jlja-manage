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
 * 资产来源
 * @Author lipeng
 * @Date 2022/4/21 17:58
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("as_asset_source")
public class AssetSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资产来源编号
     */
    @TableId
    private Long assetSourceId;

    /**
     * 资产来源名称
     */
    private String assetSourceName;

    /**
     * ‘0’ 一般 ‘3’ 盘盈 ‘5’调入 ‘9’评估
“调入”标记用来自动处理资产调拨，“盘盈”用于处理资产盘点时盘盈资产的自动判断，“评估”标记用来处理评估增值
‘0’ 一般 ‘3’ 盘盈 ‘5’调入 ‘9’评估
“调入”标记用来自动处理资产调拨，“盘盈”用于处理资产盘点时盘盈资产的自动判断，“评估”标记用来处理评估增值

     */
    private String sourceMark;

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
