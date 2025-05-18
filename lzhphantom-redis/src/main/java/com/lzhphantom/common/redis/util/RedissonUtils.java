package com.lzhphantom.common.redis.util;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RedissonUtils {
    private final RedissonClient redissonClient;
    /**
     * 获取分布式锁
     *
     * @param lockKey 锁的键
     * @param waitTime 等待加锁的时间
     * @param leaseTime 锁的持有时间
     * @return true 如果获取成功，false 获取失败
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 带锁的操作执行方法。自动获取和释放锁，支持返回结果。
     *
     * @param lockKey 锁的键
     * @param waitTime 等待加锁的时间（秒）
     * @param leaseTime 锁的持有时间（秒）
     * @param task 要执行的任务，带返回值
     * @param <T> 返回值类型
     * @return 任务执行结果
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, Supplier<T> task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            // 尝试加锁
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (locked) {
                // 执行传入的任务，并返回结果
                return task.get();
            } else {
                throw new RuntimeException("Could not acquire lock for key: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while trying to acquire lock", e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    /**
     * 带锁的操作执行方法。自动获取和释放锁，不带返回值。
     *
     * @param lockKey 锁的键
     * @param waitTime 等待加锁的时间（秒）
     * @param leaseTime 锁的持有时间（秒）
     * @param task 要执行的任务
     */
    public void executeWithLock(String lockKey, long waitTime, long leaseTime, Runnable task) {
        executeWithLock(lockKey, waitTime, leaseTime, () -> {
            task.run();
            return null;
        });
    }

    /**
     * 使用限流器实现访问控制
     *
     * @param rateLimiterKey 限流器的键
     * @param rate 每秒最大访问次数
     * @return true 如果允许访问，false 达到限制
     */
    public boolean tryAcquireRateLimiter(String rateLimiterKey, long rate) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        rateLimiter.trySetRate(RateType.OVERALL, rate, 1, RateIntervalUnit.SECONDS);
        return rateLimiter.tryAcquire();
    }

    /**
     * 生成全局唯一ID
     *
     * @param key ID 的键，用于区分不同的 ID 生成器
     * @return 全局唯一 ID
     */
    public long generateUniqueId(String key) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    // 生成UUID格式的唯一ID
    public String generateUUID() {
        return IdUtil.randomUUID();
    }
    //有时间顺序或符合某种特定格式
    //可以考虑雪花算法
    public String generateSnowflakeId(){
        return IdUtil.getSnowflake().nextIdStr();
    }

    // 生成自定义格式的订单号
    public String generateOrderId(String prefix) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("order_id:" + prefix);
        long sequence = atomicLong.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp + String.format("%06d", sequence);
    }

    /**
     * 使用分布式信号量控制资源的访问数量
     *
     * @param semaphoreKey 信号量的键
     * @param permits 允许的最大访问数
     * @return true 如果获取成功，false 获取失败
     */
    public boolean tryAcquireSemaphore(String semaphoreKey, int permits) {
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreKey);
        try {
            semaphore.trySetPermits(permits);
            return semaphore.tryAcquire();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 释放信号量
     *
     * @param semaphoreKey 信号量的键
     */
    public void releaseSemaphore(String semaphoreKey) {
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreKey);
        semaphore.release();
    }
}
