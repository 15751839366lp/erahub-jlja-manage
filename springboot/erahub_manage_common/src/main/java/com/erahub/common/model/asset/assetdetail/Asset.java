package com.erahub.common.model.asset.assetdetail;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lipeng
 * @Date 2022/5/2 12:37
 * @Version 1.0
 **/
@Data
@TableName("as_asset")
public class Asset {

    private static final long serialVersionUID = 1L;

    /**
     * 资产唯一编号
     */
    @TableId
    private Long assetUniqueId;

    /**
     * 设备统一编号
     */
    private String manufacturingNo;

    /**
     * 入帐日期
     */
    private Date recordedDate;

    /**
     * 入帐凭证
     */
    private String accountingVoucher;

    /**
     * 资产编号
     */
    private String assetId;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 资产类别编号
     */
    private String assetCategoryId;

    /**
     * 部门编号
     */
    private String sectionId;

    /**
     * 资产数量
     */
    private Long assetQuantity;

    /**
     * 计量单位
     */
    private String measureUnit;

    /**
     * 所在地点
     */
    private String place;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 设计能力
     */
    private String designCapacity;

    /**
     * 能力单位
     */
    private String capacityUnit;

    /**
     * 主要技术参数
     */
    private String technicalParameter;

    /**
     * 技术参数单位
     */
    private String technicalParameterUnit;

    /**
     * 出厂日期
     */
    private Date productionDate;

    /**
     * 出厂编号
     */
    private String factoryNumber;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产国别
     */
    private String productionCountry;

    /**
     * 交付使用日期
     */
    private Date usedDate;

    /**
     * 工程项目编号
     */
    private String assetProjectId;

    /**
     * 资产来源编号
     */
    private Long assetSourceId;

    /**
     * 产权归属编号
     */
    private Long assetOwnershipId;

    /**
     * 经济用途编号
     */
    private Long assetEconomicUseId;

    /**
     * 使用状态编号
     */
    private Long serviceConditionId;

    /**
     * 技术状态编号
     */
    private Long technicalConditionId;

    /**
     * 历史成本
     */
    private BigDecimal historicalCost;

    /**
     * 是否计提折旧    1 是  0 否
     */
    private Integer accrualDepreciation;

    /**
     * 折旧方法编号
     */
    private Long depreciationMethodId;

    /**
     * 折旧年限
     */
    private BigDecimal depreciationPeriod;

    /**
     * 预计总工作量
     */
    private BigDecimal estimatedTotalWorkload;

    /**
     * 净残值率
     */
    private BigDecimal netResidualValue;

    /**
     * 折旧提足日期
     */
    private Date depreciationWithdrawalDate;

    /**
     * 申报减少日期
     */
    private Date decreaseDate;

    /**
     * 处置日期
     */
    private Date disposalDate;

    /**
     * 清理日期
     */
    private Date liquidationDate;

    /**
     * 减少方法编号
     */
    private Long decreaseMethodId;

    /**
     * 减少原因编号
     */
    private Long decreaseReasonId;

    /**
     * 车辆牌号
     */
    private String licensePlateNumber;

    /**
     * 资产类型 0 固定资产1 无形资产
     */
    private Integer type;

    /**
     * 操作 0 无操作
     */
    private Integer operation;

    /**
     * 状态
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
