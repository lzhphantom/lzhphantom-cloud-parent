package com.lzhphantom.common.redis.util;

import com.lzhphantom.bean.common.RateLimitDto;
import com.lzhphantom.common.base.constants.RedisKeyConstants;
import com.lzhphantom.interfaces.limit.LimitDubboService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@ConditionalOnProperty(prefix = "rate.limiter", name = "enabled", havingValue = "true")
public class DynamicSlidingWindowRateLimiter {
    private final RedissonClient redissonClient;

    @DubboReference(lazy = true)
    private LimitDubboService limitDubboService;

    @Value("${lzhphantom.limit.window-size:60}")
    private long windowSize;

    @Value("${lzhphantom.limit.max-requests:100}")
    private int maxRequests;

    /**
     * 尝试获取滑动窗口限流许可
     *
     * @param apiKey 当前请求的API标识
     * @return true 如果允许访问，false 达到限制
     */
    public boolean tryAcquire(String apiKey) {
        RMap<String, RateLimitDto> rateLimitMap = redissonClient.getMap(RedisKeyConstants.RATE_LIMIT_KEY);
        RateLimitDto config = null;
        if (rateLimitMap.isEmpty()) {
            // 第一次从数据库配置
            Map<String, RateLimitDto> all = limitDubboService.getAll();
            config = getRateLimitDto(apiKey, all.containsKey(apiKey), all.get(apiKey), rateLimitMap);
        } else {
            config = getRateLimitDto(apiKey, rateLimitMap.containsKey(apiKey), rateLimitMap.get(apiKey), rateLimitMap);

        }

        if (config == null || !config.getEnabled()) {
            return true;  // 如果未配置或限流未启用，直接放行
        }

        RScoredSortedSet<Long> requestTimes = redissonClient.getScoredSortedSet(apiKey);
        long currentTime = Instant.now().getEpochSecond();

        // 删除窗口外的请求记录
        requestTimes.removeRangeByScore(0, true, currentTime - config.getWindowSize(), true);

        if (requestTimes.size() < config.getMaxRequests()) {
            // 记录当前请求时间戳
            requestTimes.add(currentTime, currentTime);
            requestTimes.expire(config.getWindowSize(), TimeUnit.SECONDS);  // 过期时间与窗口大小一致
            return true;
        } else {
            return false;
        }
    }

    private RateLimitDto getRateLimitDto(String apiKey, boolean b, RateLimitDto dto, RMap<String, RateLimitDto> rateLimitMap) {
        RateLimitDto config;
        if (b) {
            config = dto;
        }else {
            config = new RateLimitDto();
            config.setKey(apiKey);
            config.setWindowSize(windowSize);
            config.setMaxRequests(maxRequests);
            config.setEnabled(Boolean.TRUE);
            limitDubboService.addRateLimit(config);
        }
        return config;
    }
}
