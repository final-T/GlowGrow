package com.tk.gg.promotion.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * Redis SET 명령어: 키에 값을 설정합니다.
     *
     * @param key   키
     * @param value 값
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * Redis GET 명령어: 키의 값을 가져옵니다.
     *
     * @param key 키
     * @return 키에 대한 값
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * Redis DECR 명령어: 키의 값을 1 감소시킵니다.
     *
     * @param key 키
     * @return 감소된 값
     */
    public Long decrement(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    // SET 자료형 관련 메서드 (키와 값을 문자열로 처리)
    public Long sAdd(String key, String value) {
        return stringRedisTemplate.opsForSet().add(key, value);
    }

    public Long sCard(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public Boolean sIsMember(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }
}

