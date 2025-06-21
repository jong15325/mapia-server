package me.jjh.mapia.webserver.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : ServerProperties
 * author         : JJH
 * date           : 2025-03-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-01        JJH       최초 생성
 */

@Component
@ConfigurationProperties(prefix = "custom.server")
@Getter
@Setter
public class ServerProperties {

    private String host;
    private int port;

    public String getServerAddress() {
        return host + ":" + port;
    }
}
