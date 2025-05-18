package com.lzhphantom.common.redis.aop;

import cn.hutool.core.util.StrUtil;
import com.lzhphantom.common.base.constants.RedisKeyConstants;
import com.lzhphantom.common.base.exception.CommonException;
import com.lzhphantom.common.base.result.CommonResultCode;
import com.lzhphantom.common.redis.anno.Idempotent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotentAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(com.lzhphantom.common.redis.anno.Idempotent)")
    public void idempotentPointCut() {
    }

    @Around("idempotentPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        // 构建分布式锁的 Key
        String lockKey = buildLockKey(joinPoint, idempotent);
        RLock lock = redissonClient.getLock(lockKey);

        boolean acquired = false;
        try {
            // 尝试获取锁，最多等待{waitTime}秒，锁的自动过期时间为{timeout}秒
            acquired = lock.tryLock(idempotent.waitTime(), idempotent.timeout(), idempotent.timeUnit());

            if (!acquired) {
                log.warn("Could not acquire lock for key: {}", lockKey);
                throw new CommonException(CommonResultCode.IDEMPOTENT_ILLEGAL);
            }
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            // 处理锁等待期间的中断异常
            Thread.currentThread().interrupt();  // 重置中断状态
            log.error("Interrupted while acquiring lock: {}", e.getMessage());
            throw new CommonException(CommonResultCode.IDEMPOTENT_ILLEGAL);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Method method = joinPoint.getSignature().getDeclaringType().getMethod(joinPoint.getSignature().getName(),
                ((MethodSignature) joinPoint.getSignature()).getParameterTypes());
        return method;
    }

    private String buildLockKey(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        if (StrUtil.isEmpty(idempotent.key())) {
            throw new CommonException(CommonResultCode.IDEMPOTENT_KEY_NULL);
        }
        String keyPrefix = StringUtils.hasText(idempotent.key()) ? RedisKeyConstants.IDEMPOTENT_KEY + idempotent.key() : RedisKeyConstants.IDEMPOTENT_KEY;
        Object[] args = joinPoint.getArgs();
        log.info("args: {}", args);

        // 可以根据实际需求自定义 key 的生成策略
        return keyPrefix + ":" + args[0]; // 假设第一个参数是唯一的 ID，比如订单 ID
    }
}
