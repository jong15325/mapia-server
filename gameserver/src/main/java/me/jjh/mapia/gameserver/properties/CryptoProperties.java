package me.jjh.mapia.gameserver.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : CryptoProperties
 * author         : JJH
 * date           : 2025-03-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-24        JJH       최초 생성
 */

@Slf4j
@Component
@ConfigurationProperties(prefix = "crypto")
@Getter
@Setter
public class CryptoProperties {
    private String secretKey;
    private String jwtSecretKey;
}
