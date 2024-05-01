package com.remind.core.domain.common.repository;


import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    @Value("${jwt.refresh-expiration-seconds}")
    private int refreshExpirationSeconds;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * refresh token 저장
     */
    public void saveToken(Long memberId, String refreshToken) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(refreshExpirationSeconds);
        valueOperations.set(String.valueOf(memberId), Map.of("refreshToken", refreshToken), expireDuration);
    }

    /**
     * refresh token 삭제
     */
    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(String.valueOf(memberId));
    }

    /**
     * refresh token 가져 오기
     */
    public Map getRefreshToken(Long memberId) {
        return (Map) redisTemplate.opsForValue().get(String.valueOf(memberId));
    }

    /**
     * refresh token 존재 여부 확인
     */
    public Boolean hasKey(Long memberId){
        return redisTemplate.hasKey(String.valueOf(memberId));
    }
}
