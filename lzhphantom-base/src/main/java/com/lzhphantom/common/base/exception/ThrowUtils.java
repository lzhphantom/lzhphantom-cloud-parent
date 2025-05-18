package com.lzhphantom.common.base.exception;

import com.lzhphantom.common.base.result.IResultCode;

public class ThrowUtils {

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param runtimeException 异常类
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException){
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, IResultCode errorCode){
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param errorCode 错误码
     * @param msg 自定义错误消息
     */
    public static void throwIf(boolean condition, IResultCode errorCode,String msg){
        throwIf(condition, new BusinessException(errorCode,msg));
    }
}
