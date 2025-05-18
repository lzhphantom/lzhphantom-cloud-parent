package com.lzhphantom.common.web.aop;

import cn.hutool.core.util.StrUtil;
import com.lzhphantom.common.base.result.LzhphantomResult;
import com.lzhphantom.common.base.result.ResultCode;
import com.lzhphantom.common.web.anno.ExtApiIdempotent;
import com.lzhphantom.common.web.utils.RedisToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Deprecated
public class ExtApiAopIemPotent {
    private final RedisToken redisToken;

    @Pointcut("@annotation(com.lzhphantom.common.web.anno.ExtApiIdempotent)")
    public void rlAop() {

    }

    @Around("rlAop()")
    @SneakyThrows
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        ExtApiIdempotent annotation = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
        if (Objects.isNull(annotation)) {
            return proceedingJoinPoint.proceed();
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("Oo");
        if (StrUtil.isEmpty(token)){
            return LzhphantomResult.failed(ResultCode.LACK_ONLY_ONE_TOKEN_ERROR);
        }
        if (!redisToken.checkToken(token)) {
            return LzhphantomResult.failed(ResultCode.DUPLICATE_SUBMIT_ERROR);
        }
        return proceedingJoinPoint.proceed();
    }
}
