package com.tk.gg.promotion.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis Lua Script 실행
     * @param script Lua 스크립트 내용
     * @param keys Lua 스크립트에서 사용하는 키 목록
     * @param args Lua 스크립트에서 사용하는 인자 목록
     * @return Lua 스크립트 실행 결과 (Long 타입 으로 반환)
     */
    public Long executeLuaScript(String script, List<String> keys, List<String> args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        // RedisTemplate을 사용해 Lua 스크립트 실행
        return redisTemplate.execute(redisScript, keys, args);
    }

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

