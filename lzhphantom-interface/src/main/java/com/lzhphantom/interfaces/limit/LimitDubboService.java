package com.lzhphantom.interfaces.limit;


import com.lzhphantom.bean.common.RateLimitDto;

import java.util.Map;

public interface LimitDubboService {
    Map<String, RateLimitDto> getAll();
    void addRateLimit(RateLimitDto rateLimitDto);
}
