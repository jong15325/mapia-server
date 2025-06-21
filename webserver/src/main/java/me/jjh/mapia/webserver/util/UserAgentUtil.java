package me.jjh.mapia.webserver.util;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.exception.BusinessException;
import me.jjh.mapia.webserver.common.exception.ServerException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : UserAgentUtil
 * author         : JJH
 * date           : 2025-03-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-08        JJH       최초 생성
 */

@Slf4j
public class UserAgentUtil {
    private static final Parser parser;

    static {
        try {
            parser = new Parser();
        } catch (IOException e) {
            throw new RuntimeException("User-Agent Parser 초기화 실패", e);
        }
    }

    /**
     * 현재 요청의 HttpServletRequest 가져오기
     */
    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

    /**
     * User-Agent 문자열 가져오기
     */
    private static String getUserAgentString(HttpServletRequest request) {
        if (request == null) {
            request = getCurrentRequest();
        }
        return (request != null) ? request.getHeader("User-Agent") : "Unknown";
    }

    /**
     * User-Agent에서 운영체제(OS) 정보 추출
     */
    public static String getOS(HttpServletRequest request) {
        String userAgentString = getUserAgentString(request);
        if ("Unknown".equals(userAgentString)) {
            return "Unknown";
        }

        Client client = parser.parse(userAgentString);
        return client.os.family + (client.os.major != null ? " " + client.os.major : "");
    }

    /**
     * User-Agent에서 브라우저 정보 추출
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgentString = getUserAgentString(request);
        if ("Unknown".equals(userAgentString)) {
            return "Unknown";
        }

        Client client = parser.parse(userAgentString);
        return client.userAgent.family + (client.userAgent.major != null ? " " + client.userAgent.major : "");
    }

    /**
     * User-Agent에서 디바이스 정보 추출
     */
    public static String getDevice(HttpServletRequest request) {
        String userAgentString = getUserAgentString(request);
        if ("Unknown".equals(userAgentString)) {
            return "Unknown";
        }

        Client client = parser.parse(userAgentString);
        return client.device.family;
    }

    /**
     * 모바일 여부 판별 (모바일이면 true, PC면 false)
     */
    public static boolean isMobile(HttpServletRequest request) {
        String userAgentString = getUserAgentString(request);
        if ("Unknown".equals(userAgentString)) {
            return false;
        }

        Client client = parser.parse(userAgentString);
        String device = client.device.family.toLowerCase();

        return device.contains("mobile") || device.contains("iphone") || device.contains("android");
    }

    /**
     * 모든 User-Agent 정보를 하나의 문자열로 반환
     */
    public static String getUserAgentInfo(HttpServletRequest request) {
        return "OS: " + getOS(request) +
                ", Browser: " + getBrowser(request) +
                ", Device: " + getDevice(request) +
                ", Mobile: " + (isMobile(request) ? "Yes" : "No");
    }

    /**
     * 클라이언트의 내부 IP (로컬 네트워크 IP) 가져오기
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            request = getCurrentRequest();
        }

        if (request == null) {
            return "Unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 다중 프록시 사용 시 첫 번째 IP 가져오기
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

}
