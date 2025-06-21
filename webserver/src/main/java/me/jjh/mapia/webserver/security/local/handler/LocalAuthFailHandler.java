package me.jjh.mapia.webserver.security.local.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.security
 * fileName       : CustomAuthFailureHandler
 * author         : JJH
 * date           : 2025-02-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-01        JJH       최초 생성
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Map<Class<? extends AuthenticationException>, ComCode> ALERT_CODE_MAP = new HashMap<>();

    private final ApplicationEventPublisher eventPublisher; // Spring Security 이벤트 발행


    static {
        //로그인 실패 관련 예외
        ALERT_CODE_MAP.put(BadCredentialsException.class, ComCode.LOGIN_NOT_MATCH); // 잘못된 자격 증명(비밀번호 오류)
        ALERT_CODE_MAP.put(UsernameNotFoundException.class, ComCode.LOGIN_NOT_MATCH); // UserDetailsService에 조회되지 않는 아이디
        ALERT_CODE_MAP.put(LockedException.class, ComCode.LOGIN_LOCKED); // 잠긴 계정
        ALERT_CODE_MAP.put(DisabledException.class, ComCode.LOGIN_DISABLED); // 비활성화된 계정
        ALERT_CODE_MAP.put(AccountExpiredException.class, ComCode.LOGIN_SESSION_EXPIRED); // 만료된 계정 UserDetails.isAccountNonExpired()가 false
        ALERT_CODE_MAP.put(CredentialsExpiredException.class, ComCode.LOGIN_SESSION_EXPIRED); // 만료된 자격 증명 (비밀번호 만료) UserDetails.isAccountNonExpired()가 false

        // 보안 및 인증 실패 관련 예외
        ALERT_CODE_MAP.put(InsufficientAuthenticationException.class, ComCode.LOGIN_FAIL); // 현재 인증이 필요한 권한이 없음 (SecurityContext에 인증 정보가 없음)
        ALERT_CODE_MAP.put(SessionAuthenticationException.class, ComCode.LOGIN_LOCKED); // 동시 로그인 제한 등의 문제로 인해 세션 인증 실패
        ALERT_CODE_MAP.put(AuthenticationCredentialsNotFoundException.class, ComCode.LOGIN_DISABLED); // SecurityContext에 인증 객체가 존재하지 않음

        // 내부 인증 오류 관련 예외
        ALERT_CODE_MAP.put(AuthenticationServiceException.class, ComCode.LOGIN_SYSTEM_ERROR); // 인증 과정에서 내부적인 예외 발생 (DB 연결 오류, API 오류 등)
        ALERT_CODE_MAP.put(ProviderNotFoundException.class, ComCode.LOGIN_SYSTEM_ERROR); // AuthenticationManager에서 해당 인증을 처리할 수 없음
        //ALERT_CODE_MAP.put(RememberMeAuthenticationException.class, ComCode.LOGIN_FAIL); // Remember Me 쿠키를 사용한 인증이 실패한 경우
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("[LOCAL AUTH FAIL HANDLER - onAuthenticationFailure] START");

        ComCode comCode = ALERT_CODE_MAP.getOrDefault(exception.getClass(), ComCode.LOGIN_FAIL);

        // 이벤트 발행
        publishFailureEvent(request, exception);

        // Spring Security 기본 예외 정보 저장
        saveException(request, exception);

        // 인증 정보 제거
        SecurityContextHolder.clearContext();

        request.setAttribute("alertResponse", new AlertResponse(comCode, AlertType.WARNING, "/auth/login?memberId=" + request.getParameter("memberId")));
        request.setAttribute("alertType", "MOVE");
        request.getRequestDispatcher("/WEB-INF/views/common/alert/alert.jsp").forward(request, response);

        log.debug("[LOCAL AUTH FAIL HANDLER - onAuthenticationFailure] END");
    }

    /**
     * 인증 실패 이벤트를 안전하게 발행하는 유틸리티
     * @param exception
     */
    private void publishFailureEvent(HttpServletRequest request, AuthenticationException exception) {
        log.debug("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] START");

        String memberId = request.getParameter("memberId");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] 이벤트 발행 불가(인증 정보 미일치) ID [{}] msg [{}]", memberId, exception.getMessage());
            return;
        }

        try {
            if (exception instanceof LockedException) {
                log.warn("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureLockedEvent memberId[{}]", memberId);
                eventPublisher.publishEvent(new AuthenticationFailureLockedEvent(authentication, exception));
            } else if (exception instanceof DisabledException) {
                log.warn("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureDisabledEvent memberId[{}]", memberId);
                eventPublisher.publishEvent(new AuthenticationFailureDisabledEvent(authentication, exception));
            } else if (exception instanceof BadCredentialsException) {
                log.warn("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureBadCredentialsEvent memberId[{}]", memberId);
                eventPublisher.publishEvent(new AuthenticationFailureBadCredentialsEvent(authentication, exception));
            }
        } catch (Exception e) {
            log.warn("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] 이벤트 발행 불가(예외 발생) ID [{}] msg [{}]", memberId, exception.getMessage(), e);
        }

        log.debug("[LOCAL AUTH FAIL HANDLER - publishFailureEvent] END");
    }
}
