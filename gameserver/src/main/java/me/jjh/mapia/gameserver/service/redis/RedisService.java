package me.jjh.mapia.gameserver.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.util.DataVaildUtil;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    // 단일 값 저장
    public Mono<Boolean> set(String key, Object value) {
        log.warn("[REDIS SERVICE - set] START");
        log.warn("[REDIS SERVICE - set] END");
        return redisTemplate.opsForValue().set(key, value)
                .doOnSuccess(result -> log.debug("[REDIS SERVICE - set] key : {}, result : {}", key, result))
                .doOnError(error -> log.error("[REDIS SERVICE - set] key : {}, error : {}", key, error.getMessage()));
    }

    // 단일 값 조회
    public Mono<Object> get(String key) {
        log.warn("[REDIS SERVICE - get] START");
        log.warn("[REDIS SERVICE - get] END");

        return redisTemplate.opsForValue().get(key)
                .doOnNext(value -> log.debug("[REDIS SERVICE - get] key: {}, result: {}", key, value))
                .doOnError(error -> log.error("[REDIS SERVICE - get] key: {}, error: {}", key, error.getMessage()));
    }

    // 단일 키 삭제
    public Mono<Boolean> delete(String key) {
        log.warn("[REDIS SERVICE - delete] START");
        log.warn("[REDIS SERVICE - delete] END");

        return redisTemplate.delete(key)
                .map(count -> count > 0)
                .doOnSuccess(result -> log.debug("[REDIS SERVICE - delete] key: {}, result: {}", key, result))
                .doOnError(error -> log.error("[REDIS SERVICE - delete] key: {}, error: {}", key, error.getMessage()));
    }

    // 해시 필드 저장
    public Mono<Boolean> hSet(String hashKey, String field, Object value) {
        log.warn("[REDIS SERVICE - hSet] START");
        log.warn("[REDIS SERVICE - hSet] END");
        return redisTemplate.opsForHash().put(hashKey, field, value)
                .doOnSuccess(result -> log.debug("[REDIS SERVICE - hSet] key: {}, field: {}, result: {}", hashKey, field, result))
                .doOnError(error -> log.error("[REDIS SERVICE - hSet] key: {}, field: {}, error: {}", hashKey, field, error.getMessage()));
    }

    // 해시 필드 조회
    public Mono<Object> hGet(String hashKey, String field) {
        log.warn("[REDIS SERVICE - hGet] START");
        log.warn("[REDIS SERVICE - hGet] END");
        return redisTemplate.opsForHash().get(hashKey, field)
                .doOnNext(value -> log.debug("[REDIS SERVICE - hGet] key: {}, 필드: {}, value: {}", hashKey, field, value))
                .doOnError(error -> log.error("[REDIS SERVICE - hGet] key: {}, 필드: {}, 에러: {}", hashKey, field, error.getMessage()));
    }

    // 해시 전체 조회
    public Flux<Object> hGetAll(String hashKey) {
        log.warn("[REDIS SERVICE - hGetAll] START");
        log.warn("[REDIS SERVICE - hGetAll] END");
        return redisTemplate.opsForHash().values(hashKey)
                .doOnComplete(() -> log.debug("[REDIS SERVICE - hGetAll] key: {}", hashKey))
                .doOnError(error -> log.error("[REDIS SERVICE - hGetAll] key: {}, error: {}", hashKey, error.getMessage()));
    }

    // 해시 필드 삭제
    public Mono<Long> hDelete(String hashKey, String field) {
        log.warn("[REDIS SERVICE - hDelete] START");
        log.warn("[REDIS SERVICE - hDelete] END");
        return redisTemplate.opsForHash().remove(hashKey, field)
                .doOnSuccess(count -> log.debug("[REDIS SERVICE - hDelete] key: {}, field: {}, deletedCount: {}", hashKey, field, count))
                .doOnError(error -> log.error("[REDIS SERVICE - hDelete] key: {}, field: {}, error: {}", hashKey, field, error.getMessage()));
    }

    // 키 존재 여부 확인
    public Mono<Boolean> hasKey(String key) {
        log.warn("[REDIS SERVICE - hasKey] START");
        log.warn("[REDIS SERVICE - hasKey] END");
        return redisTemplate.hasKey(key)
                .doOnSuccess(result -> log.debug("[REDIS SERVICE - hasKey] key: {}, exists: {}", key, result))
                .doOnError(error -> log.error("[REDIS SERVICE - hasKey] key: {}, error: {}", key, error.getMessage()));
    }
}
