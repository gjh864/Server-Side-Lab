package com.gonjunhan.helloserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作工具类
 * 图片要求：配套工具类 (必须添加)
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间 (分钟)
     * @param key 键
     * @param value 值
     * @param time 时间(分钟)
     * @return true成功 false失败
     */
    public boolean setMinute(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取缓存
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * @param key 键
     * @return true成功 false失败
     */
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}