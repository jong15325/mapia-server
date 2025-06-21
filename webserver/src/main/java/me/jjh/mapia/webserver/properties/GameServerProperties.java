package me.jjh.mapia.webserver.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConfigurationProperties(prefix = "game.server")
@Getter
@Setter
public class GameServerProperties {
    private String host;
    private int port;

    public String getServerAddress() {
        return host + ":" + port;
    }
}
