package com.erahub.common.enums.fixedasset.metadata;

/**
 * @Author lipeng
 * @Date 2022/4/19 16:07
 * @Version 1.0
 **/
public enum FixedAssetCategoryStatusEnum {
    DISABLE(0),
    AVAILABLE(1);

    private long statusCode;

    FixedAssetCategoryStatusEnum(long statusCode) {
        this.statusCode = statusCode;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }
}
