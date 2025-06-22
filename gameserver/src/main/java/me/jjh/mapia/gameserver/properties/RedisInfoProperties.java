package me.jjh.mapia.gameserver.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Getter
@Setter
public class RedisInfoProperties {
    private String host;
    private int port;
    private String password;

    @PostConstruct
    public void init() {
        log.info("Redis Properties Loaded: host={}, port={}, password={}]",
                host, port, password);
    }
}
