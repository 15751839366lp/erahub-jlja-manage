package com.erahub.common.error.asset;

import com.erahub.common.error.BaseError;
import lombok.Getter;

/**
 * @Author lipeng
 * @Date 2022/4/21 15:08
 * @Version 1.0
 **/
@Getter
public enum AssetCodeEnum implements BaseError {

    //通用的异常以0000开头
    PARAMETER_ERROR(00001,"参数不合法"),

    // 数据操作错误定义
    BODY_NOT_MATCH(400,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(401,"请求的数字签名不匹配!"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!"),
    //用户相关：10000**
    USER_ACCOUNT_NOT_FOUND(10001, "账号不存在!"),
    DoNotAllowToDisableTheCurrentUser(10002,"不允许禁用当前用户"),

    //业务异常
    //资产类别模块
    ASSET_CATEGORY_NOT_FOUND(40101, "资产类别不存在!"),

    //折旧方法模块
    DEPRECIATION_METHOD_NOT_FOUND(40201, "折旧方法不存在!"),

    //单位模块
    SECTION_NOT_FOUND(40301, "单位不存在!"),

    //使用状态模块
    SERVICE_CONDITION_NOT_FOUND(40401, "使用单位不存在!"),

    //资产来源模块
    ASSET_SOURCE_NOT_FOUND(40501, "资产来源不存在!"),

    //减少方式模块
    REDUCE_METHOD_NOT_FOUND(40601, "减少方式不存在!"),

    //减少原因模块
    REDUCE_REASON_NOT_FOUND(40701, "减少原因不存在!"),

    //技术状态模块
    TECHNICAL_CONDITION_NOT_FOUND(40801, "技术状态不存在!");

    /** 错误码 */
    private int errorCode;

    /** 错误描述 */
    private String errorMsg;

    AssetCodeEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public BaseError setErrorMsg(String errorMsg) {
        this.errorMsg=errorMsg;
        return this;
    }

}
