package com.lzhphantom.common.base.exception;

import com.lzhphantom.common.base.result.CommonResultCode;

/**
 * 公共异常
 */
public class CommonException extends BusinessException{
    public CommonException(CommonResultCode resultCode) {
        super(resultCode);
    }
}
