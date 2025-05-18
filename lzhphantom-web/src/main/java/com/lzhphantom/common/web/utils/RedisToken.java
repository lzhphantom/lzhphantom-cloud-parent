package com.lzhphantom.common.web.utils;

import cn.hutool.core.util.IdUtil;
import com.lzhphantom.common.base.constants.GlobalConstants;
import com.lzhphantom.common.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisToken {

    private final RedisUtils redisUtils;

    public String getToken() {
        String token = GlobalConstants.ONLY_ONE + IdUtil.getSnowflakeNextIdStr();
        redisUtils.set(token, token, 60L);
        return token;
    }

    public boolean checkToken(String key) {
        if (redisUtils.hasKey(key)) {
            redisUtils.del(key);
            return true;
        } else {
            return false;
        }

    }
}
