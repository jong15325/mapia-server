package me.jjh.mapia.gameserver.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.properties.RedisInfoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final RedisInfoProperties redisInfoProperties;

    @Bean
    @Primary // Auto Configuration과의 충돌 발생 하여 우선순위 설정
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        try{
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(redisInfoProperties.getHost());
            redisConfig.setPort(redisInfoProperties.getPort());
            redisConfig.setPassword(redisInfoProperties.getPassword());

            return new LettuceConnectionFactory(redisConfig);
        } catch (Exception e) {
            log.error("Failed to create Redis connection factory: {}", e.getMessage(), e);
            throw e;
        }

    }

    @Bean
    @Primary
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // webserver와 동일한 설정으로 맞춤
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        log.info("Redis ObjectMapper 설정 완료 (webserver 호환 모드)");
        return mapper;
    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(redisObjectMapper(), Object.class);
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    @Primary
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {

        // webserver와 동일한 직렬화 설정 사용
        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(stringRedisSerializer())
                .key(stringRedisSerializer())
                .value(jackson2JsonRedisSerializer())  // Jackson2JsonRedisSerializer 사용
                .hashKey(stringRedisSerializer())
                .hashValue(jackson2JsonRedisSerializer())  // 동일하게 변경
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }


}
