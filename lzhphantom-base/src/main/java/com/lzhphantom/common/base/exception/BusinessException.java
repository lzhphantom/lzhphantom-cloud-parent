package com.lzhphantom.common.base.exception;

import com.lzhphantom.common.base.result.IResultCode;
import com.lzhphantom.common.base.result.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException{
    private final IResultCode resultCode;
    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public BusinessException(IResultCode resultCode,String message){
        super(message);
        this.resultCode = resultCode;
    }

    public BusinessException(String msg){
        super(msg);
        this.resultCode = ResultCode.SYSTEM_EXECUTION_ERROR;
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
