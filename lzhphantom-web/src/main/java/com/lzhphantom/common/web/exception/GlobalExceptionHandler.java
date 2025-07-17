package com.lzhphantom.common.web.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.lzhphantom.common.base.exception.BusinessException;
import com.lzhphantom.common.base.exception.UnauthorizedException;
import com.lzhphantom.common.base.result.LzhphantomResult;
import com.lzhphantom.common.base.result.ResultCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public LzhphantomResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因,{}", e.getMessage(), e);
        return LzhphantomResult.failed(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public LzhphantomResult<?> handleIllegalArgumentException(RuntimeException e) {
        log.error("运行异常，异常原因,{}", e.getMessage(), e);
        return LzhphantomResult.failed(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public LzhphantomResult<?> handleUnauthorized(RuntimeException e){
        log.error("运行异常，异常原因,{}", e.getMessage(), e);
        return LzhphantomResult.failed(ResultCode.TOKEN_INVALID_OR_EXPIRED);
    }

    @ExceptionHandler(BindException.class)
    public LzhphantomResult<?> handleBindException(BindException e) {
        log.error("参数绑定异常", e);
        return LzhphantomResult.failed("参数错误：" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public LzhphantomResult<?> handleException(Exception e) {
        log.error("系统异常", e);
        return LzhphantomResult.failed("系统异常，请稍后重试");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotLoginException.class)
    public LzhphantomResult<?> handleNotLoginException(NotLoginException e) {
        log.error("未登录异常", e);
        return LzhphantomResult.failed(ResultCode.TOKEN_INVALID_OR_EXPIRED);
    }
}
