package me.jjh.mapia.webserver.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.exception.OAuth2LinkReqException;
import me.jjh.mapia.webserver.common.exception.OAuth2SignupReqException;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import me.jjh.mapia.webserver.security.oauth2.response.OAuth2Response;
import me.jjh.mapia.webserver.service.member.Oauth2MemberService;
import me.jjh.mapia.webserver.util.MapUtil;
import me.jjh.mapia.webserver.util.SecurityUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.security.oauth2.handler
 * fileName       : CustomOAuth2FailureHandler
 * author         : JJH
 * date           : 2025-03-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-19        JJH       최초 생성
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Map<Class<? extends AuthenticationException>, ComCode> ALERT_CODE_MAP = new HashMap<>();

    private final ApplicationEventPublisher eventPublisher;

    static {
        ALERT_CODE_MAP.put(OAuth2AuthenticationException.class, ComCode.LOGIN_FAIL); // OAuth2 인증 실패

        //로그인 실패 관련 예외
        ALERT_CODE_MAP.put(BadCredentialsException.class, ComCode.LOGIN_NOT_MATCH); // 잘못된 자격 증명(비밀번호 오류)
        ALERT_CODE_MAP.put(UsernameNotFoundException.class, ComCode.LOGIN_NOT_MATCH); // UserDetailsService에 조회되지 않는 아이디
        ALERT_CODE_MAP.put(LockedException.class, ComCode.LOGIN_LOCKED); // 잠긴 계정
        ALERT_CODE_MAP.put(DisabledException.class, ComCode.LOGIN_DISABLED); // 비활성화된 계정
        ALERT_CODE_MAP.put(AccountExpiredException.class, ComCode.LOGIN_SESSION_EXPIRED); // 만료된 계정 UserDetails.isAccountNonExpired()가 false
        ALERT_CODE_MAP.put(CredentialsExpiredException.class, ComCode.LOGIN_SESSION_EXPIRED); // 만료된 자격 증명 (비밀번호 만료) UserDetails.isAccountNonExpired()가 false
        ALERT_CODE_MAP.put(OAuth2SignupReqException.class, ComCode.OAUTH2_SIGNUP_CONFIRM); // Oauth2 회원가입 진행 여부 alert
        ALERT_CODE_MAP.put(OAuth2LinkReqException.class, ComCode.OAUTH2_LINK_REQUIRE); // Oauth2 소셜 연동 없이 계정만 있을 경우

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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.debug("[OAUTH2 AUTH FAIL HANDLER - onAuthenticationFailure] START");

        ComCode comCode = ALERT_CODE_MAP.getOrDefault(exception.getClass(), ComCode.LOGIN_FAIL);
        log.debug("comCodecomCodecomCode " + comCode);
        
        publishFailureEvent(exception);

        saveException(request, exception);

        SecurityContextHolder.clearContext();

        if(comCode.equals(ComCode.OAUTH2_SIGNUP_CONFIRM)) {
            OAuth2Response oAuth2Response = ((OAuth2SignupReqException) exception).getOAuth2Response();

            HashMap<String, Object> data = new HashMap<>();
            data.put("oauth2Email", SecurityUtil.paramEncrypt(oAuth2Response.getEmail()));
            data.put("oauth2Provider", SecurityUtil.paramEncrypt(oAuth2Response.getProvider()));
            data.put("oauth2ProviderId", SecurityUtil.paramEncrypt(oAuth2Response.getProviderId()));

            request.setAttribute("alertType", "CONFIRM");
            request.setAttribute("alertResponse", new AlertResponse(comCode, AlertType.WARNING,
                    AlertType.AJAX, "/auth/oauth2SignupProc",
                    MapUtil.toJson(data),
                    AlertType.GET, "/auth/login"
                    ));

        } else {
            log.debug("comCodecomCodecomCode 미ㅏ느아ㅣㅁ느아ㅣㅁㄴㅇ");
            request.setAttribute("alertType", "MOVE");
            request.setAttribute("alertResponse", new AlertResponse(comCode, AlertType.WARNING, "/auth/login"));
        }

        request.getRequestDispatcher("/WEB-INF/views/common/alert/alert.jsp").forward(request, response);
        log.debug("[OAUTH2 AUTH FAIL HANDLER - onAuthenticationFailure] END");
    }

    /**
     * 인증 실패 이벤트를 발행하는 메서드
     */
    private void publishFailureEvent(AuthenticationException exception) {
        log.debug("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] START");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            if (exception instanceof LockedException) {
                log.warn("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureLockedEvent 발생");
                eventPublisher.publishEvent(new AuthenticationFailureLockedEvent(authentication, exception));
            } else if (exception instanceof DisabledException) {
                log.warn("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureDisabledEvent 발생");
                eventPublisher.publishEvent(new AuthenticationFailureDisabledEvent(authentication, exception));
            } else if (exception instanceof BadCredentialsException) {
                log.warn("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] AuthenticationFailureBadCredentialsEvent 발생");
                eventPublisher.publishEvent(new AuthenticationFailureBadCredentialsEvent(authentication, exception));
            }
        } catch (Exception e) {
            log.warn("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] 이벤트 발행 실패: {}", e.getMessage());
        }

        log.debug("[OAUTH2 AUTH FAIL HANDLER - publishFailureEvent] END");
    }
}
