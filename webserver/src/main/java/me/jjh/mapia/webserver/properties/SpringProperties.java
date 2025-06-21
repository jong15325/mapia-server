package me.jjh.mapia.webserver.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : SpringProfiles
 * author         : JJH
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        JJH       최초 생성
 */

@Component
@ConfigurationProperties(prefix = "spring")
@Getter
@Setter
public class SpringProperties {
    private Profiles profiles;

    @Getter
    @Setter
    public static class Profiles {
        private String active;
    }

}
