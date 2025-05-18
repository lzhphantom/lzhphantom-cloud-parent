package com.lzhphantom.common.redis.aop;

import com.lzhphantom.common.redis.util.DynamicSlidingWindowRateLimiter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@ConditionalOnProperty(prefix = "rate.limiter", name = "enabled", havingValue = "true")
public class SlidingRateLimitAspect {
    private final DynamicSlidingWindowRateLimiter rateLimiter;

    @Around("execution(* com.lzhphantom..*.controller..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = "rate_limit:" + joinPoint.getSignature().getDeclaringTypeName() + ":" + joinPoint.getSignature().getName();

        if (rateLimiter.tryAcquire(key)) {
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("Request rate limit exceeded for key: " + key);
        }
    }
}
