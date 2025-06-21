package me.jjh.mapia.webserver.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.security.oauth2.user.OAuth2User;
import me.jjh.mapia.webserver.service.session.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.function.Predicate;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : UserSessionUtil
 * author         : JJH
 * date           : 2025-03-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-10        JJH       최초 생성
 * 2025-04-01        JJH       IntegratedSessionService 활용하도록 수정
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class UserSessionUtil {

    private final SessionService sessionService;

    /**
     * 현재 로그인된 모든 사용자 조회
     */
    public List<String> getActiveUsers() {
        return sessionService.getActiveUsers();
    }

    /**
     * 현재 로그인한 사용자 ID 조회
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Anonymous";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof LocalAuthUser) {
            return ((LocalAuthUser) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getName();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        return "Anonymous";
    }

    /**
     * 현재 HTTP 요청의 세션 ID 조회
     */
    private String getCurrentSessionId() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();
            return request.getSession().getId();
        }
        return null;
    }

    /**
     * 현재 사용자가 로그인 상태인지 확인
     */
    public boolean isCurrentUserLoggedIn() {
        String userId = getCurrentUserId();
        if ("Anonymous".equals(userId)) {
            return false;
        }

        String sessionId = getCurrentSessionId();
        if (sessionId == null) {
            return false;
        }

        return sessionService.isSessionValid(userId, sessionId);
    }

    /**
     * 현재 사용자의 세션 정보 조회
     */
    public Optional<MemberResDTO> getCurrentUserSession() {
        String userId = getCurrentUserId();
        if ("Anonymous".equals(userId)) {
            return Optional.empty();
        }

        String sessionId = getCurrentSessionId();
        if (sessionId == null) {
            return Optional.empty();
        }

        Map<String, Object> userSessionMap = sessionService.getUserSession(userId, sessionId);
        if (userSessionMap == null || userSessionMap.isEmpty()) {
            return Optional.empty();
        }

        // ObjectMapper를 사용하여 Map을 DTO로 직접 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위해
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MemberResDTO memberDTO = objectMapper.convertValue(userSessionMap, MemberResDTO.class);

        return Optional.of(memberDTO);
    }

    /**
     * 특정 사용자의 모든 세션 ID 조회
     */
    public Set<String> getUserSessionIds(String userId) {
        if (userId == null || userId.isEmpty() || "Anonymous".equals(userId)) {
            return Set.of();
        }

        return sessionService.getUserSessionIds(userId);
    }

    /**
     * 특정 사용자의 특정 세션 만료 처리
     */
    public void expireUserSession(String userId, String sessionId) {
        if (userId == null || userId.isEmpty() || "Anonymous".equals(userId) || sessionId == null || sessionId.isEmpty()) {
            log.warn("[USER SESSION UTIL - expireUserSession] 유효하지 않은 사용자 ID 또는 세션 ID");
            return;
        }

        sessionService.logout(userId, sessionId);
        log.debug("[USER SESSION UTIL - expireUserSession] 사용자 [{}]의 세션 [{}] 만료 처리 완료", userId, sessionId);
    }

    /**
     * 특정 사용자의 모든 세션 만료 처리
     */
    public void expireAllUserSessions(String userId) {
        if (userId == null || userId.isEmpty() || "Anonymous".equals(userId)) {
            log.warn("[USER SESSION UTIL - expireAllUserSessions] 유효하지 않은 사용자 ID");
            return;
        }

        sessionService.logoutAllSessions(userId);
        log.debug("[USER SESSION UTIL - expireAllUserSessions] 사용자 [{}]의 모든 세션 만료 처리 완료", userId);
    }

    /**
     * 현재 사용자의 현재 세션 만료 처리
     */
    public void expireCurrentUserSession() {
        String userId = getCurrentUserId();
        if (!"Anonymous".equals(userId)) {
            String sessionId = getCurrentSessionId();
            if (sessionId != null) {
                expireUserSession(userId, sessionId);
            }
        }
    }

    /**
     * 특정 사용자의 세션 존재 여부 확인
     */
    public boolean isUserLoggedIn(String userId) {
        if (userId == null || userId.isEmpty() || "Anonymous".equals(userId)) {
            return false;
        }

        return sessionService.hasActiveSession(userId);
    }
}
