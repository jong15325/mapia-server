package me.jjh.mapia.webserver.service.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.RedisKeyCode;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.service.redis.RedisService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : me.jjh.mapia.service.session
 * fileName       : IntegratedSessionService
 * author         : JJH
 * date           : 2025-04-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-01        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionService {

    private final RedisService redisService;
    private final ObjectMapper redisOjectMapper;

    /**
     * 사용자 세션 생성 및 관리
     */
    public void createSession(MemberResDTO memberDTO, HttpServletRequest request) {
        log.debug("[SESSION SERVICE - createSession] START");

        String memberId = memberDTO.getMemberId();
        String sessionId = request.getSession().getId(); // 고유 세션 ID 가져오기
        String sessionKey = memberId + ":" + sessionId;

        redisService.set(RedisKeyCode.USER_SESSION.getType(), sessionKey, memberDTO,
                RedisKeyCode.USER_SESSION.getDuration());

        // 이 사용자에게 어떤 세션이 속하는지 매핑 유지
        updateUserSessionMapping(memberId, sessionId, true);

        log.debug("[SESSION SERVICE - createSession] END");
    }

    // 각 사용자에게 어떤 세션이 속하는지 추적하는 메서드 추가
    private void updateUserSessionMapping(String memberId, String sessionId, boolean add) {
        log.debug("[SESSION SERVICE - updateUserSessionMapping] START");
        Object existingMapping = redisService.get(RedisKeyCode.USER_SESSION_MAP.getType(), memberId);

        if (add) {
            if (existingMapping != null) {
                String sessionKey = memberId + ":" + existingMapping;
                redisService.delete(RedisKeyCode.USER_SESSION.getType(), sessionKey);
            }

            redisService.set(RedisKeyCode.USER_SESSION_MAP.getType(), memberId, sessionId, RedisKeyCode.USER_SESSION.getDuration());
        } else {
            // 세션 제거 (현재 세션 ID와 매핑된 세션 ID가 일치할 때만 삭제)
            if (existingMapping != null && existingMapping.toString().equals(sessionId)) {
                String sessionKey = memberId + ":" + existingMapping;
                redisService.delete(RedisKeyCode.USER_SESSION.getType(), sessionKey);
                redisService.delete(RedisKeyCode.USER_SESSION_MAP.getType(), memberId);
            }
        }
        log.debug("[SESSION SERVICE - updateUserSessionMapping] END");
    }

    /**
     * 로그아웃 처리
     */
    public void logout(String memberId, String sessionId) {
        log.debug("[SESSION SERVICE - logout] START");

        // 특정 세션 삭제
        String sessionKey = memberId + ":" + sessionId;
        redisService.delete(RedisKeyCode.USER_SESSION.getType(), sessionKey);

        // 이 사용자에게 어떤 세션이 속하는지 매핑 유지
        updateUserSessionMapping(memberId, sessionId, false);

        log.debug("[SESSION SERVICE - logout] 사용자 로그아웃 완료 [{}]", memberId);
        log.debug("[SESSION SERVICE - logout] END");

    }

    // 원래 메서드를 유지하되 모든 세션을 지우도록 수정
    public void logoutAllSessions(String memberId) {
        log.debug("[SESSION SERVICE - logoutAllSessions] START");

        // 이 사용자의 모든 세션 가져오기
        Object existingMapping = redisService.get(RedisKeyCode.USER_SESSION_MAP.getType(), memberId);

        if (existingMapping instanceof Set) {
            Set<String> sessions = (Set<String>) existingMapping;
            for (String sessionId : sessions) {
                String sessionKey = memberId + ":" + sessionId;
                redisService.delete(RedisKeyCode.USER_SESSION.getType(), sessionKey);
            }
        }

        // 매핑 지우기
        redisService.delete(RedisKeyCode.USER_SESSION_MAP.getType(), memberId);

        log.debug("[SESSION SERVICE] 사용자의 모든 세션 로그아웃 [{}]", memberId);
        log.debug("[SESSION SERVICE - logoutAllSessions] END");
    }

    /**
     * 특정 세션이 유효한지 확인
     */
    public boolean isSessionValid(String memberId, String sessionId) {
        String sessionKey = memberId + ":" + sessionId;
        return redisService.exists(RedisKeyCode.USER_SESSION.getType(), sessionKey);
    }

    /**
     * 특정 사용자의 활성 세션이 있는지 확인
     */
    public boolean hasActiveSession(String memberId) {
        return redisService.exists(RedisKeyCode.USER_SESSION.getType(), memberId);
    }

    /**
     * 특정 사용자의 모든 세션 ID 조회
     */
    public Set<String> getUserSessionIds(String memberId) {
        log.debug("[SESSION SERVICE - getUserSessionIds] START");

        Object existingMapping = redisService.get(RedisKeyCode.USER_SESSION_MAP.getType(), memberId);

        if (existingMapping instanceof Set) {
            return new HashSet<>((Set<String>) existingMapping);
        }

        log.debug("[SESSION SERVICE - getUserSessionIds] END");

        return new HashSet<>();
    }

    /**
     * 현재 활성화된 모든 사용자 목록 조회
     */
    public List<String> getActiveUsers() {
        return redisService.getKeysWithPattern(RedisKeyCode.USER_SESSION.getType() + ":*")
                .stream()
                .map(key -> key.substring((RedisKeyCode.USER_SESSION.getType() + ":").length()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자의 세션 정보 조회
     */
    public Map<String, Object> getUserSession(String memberId, String sessionId) {
        log.debug("[SESSION SERVICE - getUserSession] START");

        if (memberId == null || memberId.isEmpty() || sessionId == null || sessionId.isEmpty()) {
            return null;
        }

        String sessionKey = memberId + ":" + sessionId;
        Object sessionData = redisService.get(RedisKeyCode.USER_SESSION.getType(), sessionKey);
        log.debug("sessionDatasessionDatasessionData : " + sessionData);

        if (DataVaildUtil.isObjectEmpty(sessionData)) {
            return null;
        }

        Map<String, Object> redisMap = redisOjectMapper.convertValue(sessionData, new TypeReference<Map<String, Object>>() {});

        log.debug("[SESSION SERVICE - getUserSession] END");

        return redisMap;
    }
}
