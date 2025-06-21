package me.jjh.mapia.webserver.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.properties
 * fileName       : FileProperties
 * author         : JJH
 * date           : 2025-04-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-09        JJH       최초 생성
 */

@Slf4j
@Component
@ConfigurationProperties(prefix = "file.upload")
@Getter
@Setter
public class FileProperties {
    private String path;
    private String tempPath;
    private List<String> allowedExtensions;
    private int maxCountPerPost;
    private long maxFileSize;
    private long minFileSize;
    private List<String> imageExtensions;
    private List<String> documentExtensions;
    private List<String> archiveExtensions;
    private List<String> mediaExtensions;
    private Map<String, String> typeIcons;
    private String directoryFormat;
    private String namingStrategy;

    @PostConstruct
    public void init() {
        log.info("FileProperties initialized with path: {}", path);
        log.info("Max file size: {} bytes", maxFileSize);
        log.info("Max files per post: {}", maxCountPerPost);
        log.info("File naming strategy: {}", namingStrategy);

        // 업로드 디렉토리 생성
        try {
            Path uploadPath = Paths.get(path);
            Path tempUploadPath = Paths.get(tempPath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath);
            }

            if (!Files.exists(tempUploadPath)) {
                Files.createDirectories(tempUploadPath);
                log.info("Created temp upload directory: {}", tempUploadPath);
            }
        } catch (Exception e) {
            log.error("Error creating upload directories", e);
        }

        // 설정 유효성 검증
        validateConfiguration();
    }

    /**
     * 설정값 유효성 검증
     */
    private void validateConfiguration() {
        // 최소/최대 파일 크기 검증
        if (minFileSize <= 0) {
            log.warn("Invalid minFileSize ({}). Setting to default: 1024 bytes", minFileSize);
            minFileSize = 1024;
        }

        if (maxFileSize <= 0) {
            log.warn("Invalid maxFileSize ({}). Setting to default: 104857600 bytes (100MB)", maxFileSize);
            maxFileSize = 104857600; // 100MB
        }

        if (minFileSize >= maxFileSize) {
            log.warn("minFileSize ({}) is greater than or equal to maxFileSize ({}). Adjusting minFileSize.",
                    minFileSize, maxFileSize);
            minFileSize = 1024; // 1KB
        }

        // 파일 네이밍 전략 검증
        if (namingStrategy == null ||
                (!namingStrategy.equals("uuid") &&
                        !namingStrategy.equals("timestamp") &&
                        !namingStrategy.equals("original"))) {
            log.warn("Invalid namingStrategy ({}). Setting to default: uuid", namingStrategy);
            namingStrategy = "uuid";
        }

        // 허용 확장자 목록 확인
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            log.warn("No allowed extensions specified. File uploads might be restricted.");
        }

        // 디렉토리 포맷 검증
        if (directoryFormat == null || directoryFormat.isEmpty()) {
            log.warn("Invalid directoryFormat. Setting to default: yyyy/MM/dd");
            directoryFormat = "yyyy/MM/dd";
        }
    }

    /**
     * 파일 확장자가 허용된 확장자인지 확인
     * @param extension 확인할 파일 확장자
     * @return 허용 여부
     */
    public boolean isAllowedExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        return allowedExtensions.contains(extension.toLowerCase());
    }
}
