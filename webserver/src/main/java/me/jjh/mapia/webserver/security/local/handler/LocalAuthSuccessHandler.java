package me.jjh.mapia.webserver.security.local.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.service.session.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * packageName    : me.jjh.mapia.security.local.handler
 * fileName       : LocalAuthSuccessHandler
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
public class LocalAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SessionService sessionService;

    // 생성자에서 초기화
    public LocalAuthSuccessHandler(SessionService sessionService) {
        this.sessionService = sessionService;
        setDefaultTargetUrl("/");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.debug("[LOCAL AUTH SUCCESS HANDLER - onAuthenticationSuccess] START");

        try {
            // 인증 에러 세션 속성 제거
            clearAuthenticationAttributes(request);

            if (authentication.getPrincipal() instanceof LocalAuthUser localUser) {

                // 세션 생성
                sessionService.createSession(localUser.getMemberResDTO(), request);

                log.debug("[LOCAL AUTH SUCCESS HANDLER] 로컬 인증 성공, 세션 생성 완료: {}", localUser.getUsername());
            } else {
                log.warn("[LOCAL AUTH SUCCESS HANDLER] 예상치 못한 Principal 타입: {}",
                        authentication.getPrincipal() != null ?
                                authentication.getPrincipal().getClass().getName() : "null");
            }
        } catch (Exception e) {
            log.error("[LOCAL AUTH SUCCESS HANDLER] 세션 생성 중 오류 발생", e);
        }

        log.debug("[LOCAL AUTH SUCCESS HANDLER - onAuthenticationSuccess] END");

        // 기본 성공 처리 (리다이렉트 등)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
