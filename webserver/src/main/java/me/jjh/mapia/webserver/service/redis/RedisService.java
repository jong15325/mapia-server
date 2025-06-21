package me.jjh.mapia.webserver.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.exception.ServerException;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * packageName    : me.jjh.mapia.service.redis
 * fileName       : RedisService
 * author         : JJH
 * date           : 2025-03-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-07        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 데이터 저장
    public void set(String key, String keyValue, Object value, Duration duration) {
        log.debug("[REDIS SERVICE - set] START");

        String fullKey = key + (keyValue != null && !DataVaildUtil.isStringEmpty(keyValue) ? ":" + keyValue : "");

        try {
            if(duration != null) {
                redisTemplate.opsForValue().set(fullKey, value, duration);
                log.debug("[REDIS SERVICE - set] REDIS 데이터 저장 완료 key[{}] value[{}] exp[{}]", fullKey, value, duration.toMillis());
            } else {
                redisTemplate.opsForValue().set(fullKey, value);
                log.debug("[REDIS SERVICE - set] REDIS 데이터 저장 완료 key[{}] value[{}]", fullKey, value);
            }
        } catch (ServerException e) {
            log.warn("[REDIS SERVICE - set] REDIS 데이터 저장 중 문제가 발생했습니다 key[{}] message[{}]", fullKey, e.getMessage(), e);
            throw new ServerException(ErrorCode.REDIS_SAVE_FAILED);
        } finally {
            log.debug("[REDIS SERVICE - set] END");
        }
    }

    // 데이터 조회
    public Object get(String key, String keyValue) {
        log.debug("[REDIS SERVICE - get] START");

        String fullKey = key + (keyValue != null && !DataVaildUtil.isStringEmpty(keyValue) ? ":" + keyValue : "");

        try {
            Object value = redisTemplate.opsForValue().get(fullKey);
            log.debug("[REDIS SERVICE - get] REDIS 데이터 불러오기 완료 key[{}] value[{}]", fullKey, value);
            return value;
        } catch (ServerException e) {
            log.warn("[REDIS SERVICE - get] REDIS 데이터를 불러오는 중 문제가 발생했습니다 key[{}] message[{}]", fullKey, e.getMessage(), e);
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            log.debug("[REDIS SERVICE - get] END");
        }
    }

    // 데이터 삭제 (복합키)
    public Boolean delete(String key, String keyValue) {
        log.debug("[REDIS SERVICE - delete] START");

        String fullKey = key + (keyValue != null && !DataVaildUtil.isStringEmpty(keyValue) ? ":" + keyValue : "");

        try {
            Boolean result = redisTemplate.delete(fullKey);
            log.debug("[REDIS SERVICE - delete] REDIS 데이터 삭제 완료 key[{}]", fullKey);
            return result;
        } catch (ServerException e) {
            log.warn("[REDIS SERVICE - delete] REDIS 데이터 삭제 중 문제가 발생했습니다 key[{}] message[{}]", fullKey, e.getMessage(), e);
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            log.debug("[REDIS SERVICE - delete] END");
        }
    }

    // 키 존재 여부 확인
    public boolean exists(String key, String keyValue) {

        String fullKey = key + (keyValue != null && !DataVaildUtil.isStringEmpty(keyValue) ? ":" + keyValue : "");

        try {
            Boolean result = redisTemplate.hasKey(fullKey);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("[REDIS SERVICE - exists] 키 존재 확인 중 오류 발생 [key: {}]", key, e);
            throw e;
        }
    }

    // 키 존재 여부 확인 (복합키)
    public boolean hasKey(String key, String keyValue) {
        try {
            String fullKey = key + ":" + keyValue;
            Boolean result = redisTemplate.hasKey(fullKey);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Failed to check key existence in Redis: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 만료 시간 설정
    public Boolean expire(String key, Duration duration) {
        try {
            Boolean result = redisTemplate.expire(key, duration.toMillis(), TimeUnit.MILLISECONDS);
            log.debug("Expiration set for key: {} with duration: {}", key, duration.toMillis());
            return result;
        } catch (Exception e) {
            log.error("Failed to set expiration for key: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 만료 시간 설정 (복합키)
    public Boolean expire(String key, String keyValue, Duration duration) {
        try {
            String fullKey = key + ":" + keyValue;
            Boolean result = redisTemplate.expire(fullKey, duration.toMillis(), TimeUnit.MILLISECONDS);
            log.debug("Expiration set for key: {} with duration: {}", fullKey, duration.toMillis());
            return result;
        } catch (Exception e) {
            log.error("Failed to set expiration for key: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 만료 시간 조회
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to get expiration for key: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 만료 시간 조회 (복합키)
    public Long getExpire(String key, String keyValue) {
        try {
            String fullKey = key + ":" + keyValue;
            return redisTemplate.getExpire(fullKey, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to get expiration for key: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 패턴으로 키 조회
    public Set<String> getKeys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("Failed to get keys by pattern: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 패턴으로 키 삭제
    public Long deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long deleted = redisTemplate.delete(keys);
                log.debug("Deleted {} keys matching pattern: {}", deleted, pattern);
                return deleted;
            }
            return 0L;
        } catch (Exception e) {
            log.error("Failed to delete keys by pattern: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 패턴에 맞는 모든 키 조회
     * @param pattern 키 패턴 (예: "USER_SESSION:*")
     * @return 패턴에 맞는 키 목록
     */
    public List<String> getKeysWithPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys != null ? new ArrayList<>(keys) : Collections.emptyList();
        } catch (Exception e) {
            log.error("[REDIS SERVICE - getKeysWithPattern] 키 조회 중 오류 발생 [pattern: {}]", pattern, e);
            return Collections.emptyList();
        }
    }
}
