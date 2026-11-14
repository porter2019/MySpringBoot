package com.xxx.myspringboot.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import java.util.Collections;
import java.util.UUID;

/**
 * Redis 帮助类
 */
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // 类内常量
    private static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "  return redis.call('del', KEYS[1]) " +
                    "else return 0 end";

    private static final RedisScript<Long> UNLOCK_SCRIPT =
            new DefaultRedisScript<>(UNLOCK_LUA, Long.class);

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ==================== 通用操作 ====================

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除 key（单个）
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除 key（批量）
     */
    public long delete(Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count == null ? 0 : count;
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, Duration timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout));
    }

    /**
     * 获取剩余过期时间（秒），返回 -1 表示永不过期，-2 表示 key 不存在
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? -2 : expire;
    }

    // ==================== Hash 操作 ====================

    /**
     * 设置 Hash 字段
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 获取 Hash 字段
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取整个 Hash
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除 Hash 字段
     */
    public long hDelete(String key, Object... fields) {
        Long count = redisTemplate.opsForHash().delete(key, fields);
        return count == null ? 0 : count;
    }

    // ==================== List 操作 ====================

    /**
     * 从左侧压入 List
     */
    public long lPush(String key, Object value) {
        Long count = redisTemplate.opsForList().leftPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 从右侧压入 List
     */
    public long rPush(String key, Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 获取 List 指定范围
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 从左侧弹出
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右侧弹出
     */
    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    // ==================== Set 操作 ====================

    /**
     * 添加 Set 成员
     */
    public long sAdd(String key, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 获取 Set 所有成员
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断是否为 Set 成员
     */
    public boolean sIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 删除 Set 成员
     */
    public long sRemove(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    // ==================== 原子操作（并发控制必需）====================

    /**
     * 原子自增，返回自增后的值。
     * key 不存在时从 0 开始，自增后为 1。
     */
    public long increment(String key) {
        Long v = redisTemplate.opsForValue().increment(key);
        return v == null ? 0L : v;
    }

    public long increment(String key, long delta) {
        Long v = redisTemplate.opsForValue().increment(key, delta);
        return v == null ? 0L : v;
    }

    /**
     * 仅当 key 不存在时设置，返回是否设置成功（SETNX / SET IF ABSENT）。
     * 用于初始化序号、实现简单分布式锁。
     */
    public boolean setIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * SETNX + 过期时间（原子），避免设置 key 后进程崩溃导致永不过期。
     */
    public boolean setIfAbsent(String key, Object value, Duration timeout) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout));
    }

    /**
     * 仅当 key 存在时才设置 value，返回是否成功。
     * 常用于"锁续期/比对后更新"。
     */
    public boolean setIfPresent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfPresent(key, value));
    }

    /**
     * 执行 Lua 脚本（保证多步操作原子性）。
     * 分布式锁释放、复杂序号逻辑都靠它。
     */
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    // ==================== 分布式锁 ====================

    /**
     * 尝试加锁（不等待）。
     *
     * @param key 锁 key
     * @param ttl 锁自动过期时间（防死锁，务必设置）
     * @return 成功返回 token（解锁凭证），失败返回 null
     */
    public String tryLock(String key, Duration ttl) {
        String token = UUID.randomUUID().toString();
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(key, token, ttl);
        return Boolean.TRUE.equals(ok) ? token : null;
    }

    /**
     * 自旋等待加锁。
     *
     * @param key         锁 key
     * @param ttl         锁自动过期时间
     * @param waitTimeout 最长等待时间
     * @return token
     * @throws IllegalStateException 等待超时
     */
    public String lock(String key, Duration ttl, Duration waitTimeout) {
        long deadline = System.currentTimeMillis() + waitTimeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            String token = tryLock(key, ttl);
            if (token != null) {
                return token;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("等待锁被中断", e);
            }
        }
        throw new IllegalStateException("获取锁超时: " + key);
    }

    /**
     * 释放锁：Lua 保证"比对 token 再删除"原子执行，
     * 避免锁过期后误删别人持有的锁。
     *
     * @return true 表示确实由当前 token 释放
     */
    public boolean unlock(String key, String token) {
        if (token == null) {
            return false;
        }
        Long result = redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(key),
                token);
        return result != null && result > 0;
    }
}
