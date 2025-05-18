package com.lzhphantom.common.base.exception;

import com.lzhphantom.common.base.result.IResultCode;

/**
 * 认证异常
 */
public class UnauthorizedException extends RuntimeException{
    private final IResultCode resultCode;
    public UnauthorizedException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public String getErrorCode() {
        return this.resultCode.getCode();
    }

    public String getErrorMessage() {
        return this.resultCode.getMsg();
    }

    @Override
    public String toString() {
        return this.getClass().getName()+"{" +
                "errorCode='" + getErrorCode() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
