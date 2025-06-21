package me.jjh.mapia.webserver.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.security.oauth2.user.OAuth2User;
import me.jjh.mapia.webserver.service.session.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * packageName    : me.jjh.mapia.security.oauth2.handler
 * fileName       : OAuth2SuccessHandler
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
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SessionService sessionService;

    // 생성자에서 초기화
    public OAuth2SuccessHandler(SessionService sessionService) {
        this.sessionService = sessionService;
        setDefaultTargetUrl("/");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("[OAUTH2 SUCCESS HANDLER - onAuthenticationSuccess] START");

        try {
            // 인증 에러 세션 속성 제거
            clearAuthenticationAttributes(request);

            // 사용자 정보 확인 및 세션 생성
            if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
                MemberResDTO memberResDTO = oauthUser.getMemberResDTO();
                String providerType = request.getParameter("clientRegistrationId");

                // 세션 생성
                sessionService.createSession(memberResDTO, request);

                log.debug("[OAUTH2 SUCCESS HANDLER] OAuth2 인증 성공, 세션 생성 완료: userId[{}], provider[{}]",
                        memberResDTO.getMemberId(), providerType != null ? providerType : "unknown");
            } else {
                log.warn("[OAUTH2 SUCCESS HANDLER] 예상치 못한 Principal 타입: {}",
                        authentication.getPrincipal() != null ?
                                authentication.getPrincipal().getClass().getName() : "null");
            }
        } catch (Exception e) {
            log.error("[OAUTH2 SUCCESS HANDLER] 세션 생성 중 오류 발생", e);
        }

        log.debug("[OAUTH2 SUCCESS HANDLER - onAuthenticationSuccess] END");

        // 기본 성공 처리 (리다이렉트)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
