package me.jjh.mapia.webserver.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : MailProperties
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private Map<String, String> properties; // mail은 옵션때문에 Map이 더 효율적
}
