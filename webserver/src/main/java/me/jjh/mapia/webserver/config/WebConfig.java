package me.jjh.mapia.webserver.config;

import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.properties.FileProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * packageName    : me.jjh.mapia.config
 * fileName       : WebConfig
 * author         : JJH
 * date           : 2025-04-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-18        JJH       최초 생성
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드 폴더 매핑
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + properties.getPath())
                .setCachePeriod(3600);

        // static 폴더 내 모든 리소스 명시적 매핑
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(0);

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCachePeriod(0);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(0);
    }
}
