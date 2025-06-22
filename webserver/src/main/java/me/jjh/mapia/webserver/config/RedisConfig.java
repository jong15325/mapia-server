package me.jjh.mapia.webserver.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.properties.RedisInfoProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * packageName    : me.jjh.mapia.redis
 * fileName       : RedisConfig
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
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7 * 24 * 60 * 60) // 7일 세션 유지
public class RedisConfig {

    private final RedisInfoProperties redisInfoProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        try {
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(redisInfoProperties.getHost());
            redisConfig.setPort(redisInfoProperties.getPort());
            redisConfig.setPassword(redisInfoProperties.getPassword());

            LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                    .poolConfig(genericObjectPoolConfig())
                    .build();

            return new LettuceConnectionFactory(redisConfig, clientConfig);
        } catch (Exception e) {
            log.error("Failed to create Redis connection factory: {}", e.getMessage(), e);
            throw e;
        }
    }

    private GenericObjectPoolConfig<?> genericObjectPoolConfig() {
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(redisInfoProperties.getLettuce().getPool().getMaxActive());
        poolConfig.setMaxIdle(redisInfoProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisInfoProperties.getLettuce().getPool().getMinIdle());
        return poolConfig;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());

            // Key Serializer 설정
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());

            // Value Serializer 설정 (JSON으로 직렬화/역직렬화)
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.activateDefaultTyping(
                    LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            );
            Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

            template.setValueSerializer(serializer);
            template.setHashValueSerializer(serializer);

            log.debug("[RedisConfig - redisTemplate] 레디스 템플릿 생성에 성공했습니다");
            return template;
        } catch (Exception e) {
            log.warn("[RedisConfig - redisTemplate] 레디스 템플릿 생성에 실패했습니다 [{}]", e.getMessage(), e);
            throw e;
        }
    }
}
