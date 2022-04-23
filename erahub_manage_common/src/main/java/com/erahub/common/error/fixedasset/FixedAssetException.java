package com.erahub.common.error.fixedasset;

import com.erahub.common.error.BaseError;
import lombok.Data;

/**
 * @Author lipeng
 * @Date 2022/4/21 15:08
 * @Version 1.0
 **/
public class FixedAssetException extends Exception implements BaseError {

    //所有实现了BaseError的ErrorEnum.
    private BaseError baseError;

    //直接构造错误消息的构造异常
    public FixedAssetException(BaseError baseError){
        super(baseError.getErrorMsg());
        this.baseError=baseError;
    }

    //自定义错误消息的构造异常
    public FixedAssetException(BaseError baseError,String customErrorMessage){
        super();
        this.baseError=baseError;
        this.baseError.setErrorMsg(customErrorMessage);
    }

    @Override
    public int getErrorCode() {
        return this.baseError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.baseError.getErrorMsg();
    }

    @Override
    public BaseError setErrorMsg(String message) {
        this.baseError.setErrorMsg(message);
        return this;
    }
}
