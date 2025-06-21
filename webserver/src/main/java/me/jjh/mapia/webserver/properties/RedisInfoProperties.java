package me.jjh.mapia.webserver.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : RedisProperties
 * author         : JJH
 * date           : 2025-03-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-07        JJH       최초 생성
 */

@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Getter
@Setter
public class RedisInfoProperties {
    private String host;
    private int port;
    private String password;

    private RedisProperties.Lettuce lettuce;

    @Getter
    @Setter
    public static class Lettuce {
        private Pool pool;
    }

    @Getter
    @Setter
    public static class Pool {
        private int maxActive;
        private int maxIdle;
        private int minIdle;
    }

    @PostConstruct
    public void init() {
        log.info("Redis Properties Loaded: host={}, port={}, password={}]",
                host, port, password);
    }
}
