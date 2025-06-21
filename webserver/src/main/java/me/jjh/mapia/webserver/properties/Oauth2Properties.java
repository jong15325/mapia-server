package me.jjh.mapia.webserver.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : Oauth2Properties
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
@ConfigurationProperties(prefix = "oauth2")
@Getter
@Setter
public class Oauth2Properties {
    private Naver naver;
    private Kakao kakao;

    @Getter
    @Setter
    public static class Naver {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authUri;
        private String tokenUri;
        private String userInfoUri;
        private List<String> scope;
    }

    @Getter
    @Setter
    public static class Kakao {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authUri;
        private String tokenUri;
        private String userInfoUri;
        private List<String> scope;
    }

}
