package com.lzhphantom.common.redis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lzhphantom.common.base.exception.BusinessException;
import com.lzhphantom.common.base.result.ResultCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间（秒）
     * @return true 成功 false 失败
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键
     * @return 时间（秒）返回0代表永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false 不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个或多个键
     */
    public void del(String... key) {
        if (Objects.nonNull(key) && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((List<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    // =====================================String（字符串）==============================================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return Objects.isNull(key) ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几（大于0）
     * @return unknown
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几（大于0）
     * @return unknown
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //========================================HASH（哈希）========================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(@NonNull String key, @NonNull String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public Map hget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hashKey对应所有键值
     *
     * @param key 键
     * @return 对应的多个值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间（秒）
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map map, long time) {
        try {

            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, Map value) {
        try {
            redisTemplate.opsForHash().putAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间（秒） ！注意：如果已存在的hash表有时间，这集将会替换原有的时间
     * @return true 成功 false 失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为空null
     * @param item 项 可以多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中室友存在该项的值
     *
     * @param key  键
     * @param item 项
     * @return true 存在 false 不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在，就会创建一个，并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几（大于0）
     * @return unknown
     */
    public double hincr(String key, String item, double by) {
        if (by < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少几（大于0）
     * @return unknown
     */
    public double hdecr(String key, String item, double by) {
        if (by < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // =====================================Set(集合)===========================================

    /**
     * 根据key 获取Set中的所有值
     *
     * @param key 键
     * @return 值集合
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value 从一个set中查询，是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false 不存在
     */
    public boolean sHasKey(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 将数据放入set中
     *
     * @param key    键
     * @param values 值 可以多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间（秒）
     * @param values 值 可以放多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为values的值
     *
     * @param key    键
     * @param values 值 可以多个
     * @return 移除个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //=======================================List(列表)===========================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1 代表所有值
     * @return 值列表
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index >=0 时，0 表头，1第二个元素； index<0时，-1 表尾，-2倒数第二个元素，以此类推
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒）
     * @return true 成功 false 失败
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒）
     * @return true 成功 false 失败
     */
    public boolean lSetAllList(String key, List<?> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据所有修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据正则表达式获取key列表
     *
     * @param patternKey 正则表达式
     * @return 匹配key列表
     */
    public Set<String> keys(String patternKey) {
        try {
            return redisTemplate.keys(patternKey);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public List<LinkedHashMap<String,Object>> getList(String key) {
        try {
            List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
            if (CollUtil.isEmpty(list)){
                return CollUtil.newArrayList();
            }
            return (List<LinkedHashMap<String,Object>>) list.get(0);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SYSTEM_EXECUTION_ERROR);
        }
    }

    public <T> List<T> zGet(String key, Class<T> clazz){
        try {
            Set<Object> set = redisTemplate.opsForZSet().range(key, 0, -1);
            return BeanUtil.copyToList(set,clazz);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SYSTEM_EXECUTION_ERROR);
        }

    }
}
