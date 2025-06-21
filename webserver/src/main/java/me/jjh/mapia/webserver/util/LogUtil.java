package me.jjh.mapia.webserver.util;

import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.builder.LogBuilder;
import me.jjh.mapia.webserver.common.response.ErrorResponse;
import me.jjh.mapia.webserver.properties.SpringProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : LogUtil
 * author         : JJH
 * date           : 2025-01-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-17        JJH       최초 생성
 */

@Slf4j
@Component
public class LogUtil implements ApplicationContextAware {

    /**
     * ApplicationContextAware interface를 사용해서 logutil을 전역적으로 사용하고 profiles를 주입
     */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void exception(ErrorResponse response, String header) {
        SpringProperties springProperties = context.getBean(SpringProperties.class);

        boolean isActive = "local".equalsIgnoreCase(springProperties.getProfiles().getActive());

        LogBuilder builder = new LogBuilder()
                .addField("STATUS", response.getStatus())
                .addField("REASON PHRASE", response.getReasonPhrase())
                .addField("CODE", response.getCode())
                .addField("TIMESTAMP", response.getTimestamp())
                .addField("PATH", response.getPath())
                .addField("METHOD", response.getMethod())
                .addField("ADD_MESSAGE", response.getAddMessage())
                .addFieldIf(isActive, "EX_MESSAGE", response.getExMessage())
                .addFieldIf(isActive, "CLASS_NAME", response.getClassName())
                .addFieldIf(isActive, "LOCATION", response.getLocation())
                .addFieldIf(isActive, "METHOD_NAME", response.getMethodName())
                .addFieldIf(isActive, "LINE_NUMBER", response.getLineNumber())
                .addFieldIf(isActive, "USER_ID", response.getUserId())
                .addFieldIf(isActive, "CLIENT_IP", response.getClientIp())
                .addFieldIf(isActive, "USER_AGENT", response.getUserAgent())
                ;


        log.warn(builder.build("[EXCEPTION - " + header + "]", response.getMessage()));

    }

    public static void error(ErrorResponse response, String header) {
        SpringProperties springProperties = context.getBean(SpringProperties.class);

        boolean isActive = "local".equalsIgnoreCase(springProperties.getProfiles().getActive());

        LogBuilder builder = new LogBuilder()
                .addField("STATUS", response.getStatus())
                .addField("TIMESTAMP", response.getTimestamp())
                .addField("PATH", response.getPath())
                .addField("METHOD", response.getMethod())
                .addFieldIf(isActive, "USER_ID", response.getUserId())
                .addFieldIf(isActive, "CLIENT_IP", response.getClientIp())
                .addFieldIf(isActive, "USER_AGENT", response.getUserAgent())
                ;

        log.warn(builder.build("[EXCEPTION - " + header + "]", response.getMessage()));
    }
}
