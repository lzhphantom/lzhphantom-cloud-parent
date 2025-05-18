package com.lzhphantom.common.redis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate.limiter")
@Getter
@Setter
public class RateLimiterProperties {
    private boolean enabled = true;
}