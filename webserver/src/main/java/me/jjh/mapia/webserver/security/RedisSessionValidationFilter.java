package me.jjh.mapia.webserver.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.security.oauth2.user.OAuth2User;
import me.jjh.mapia.webserver.service.session.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * packageName    : me.jjh.mapia.security
 * fileName       : RedisSessionValidationFilter
 * author         : JJH
 * date           : 2025-04-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-10        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
public class RedisSessionValidationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    // 세션 검증에서 제외할 URL 패턴들
    private final List<String> excludeUrlPatterns = Arrays.asList(
            "/auth/login",
            "/auth/logout",
            "/auth/signup",
            "/auth/signupProc",
            "/auth/signupVerify",
            "/auth/signupVerifyResend",
            "/auth/signupVerifyProc",
            "/auth/oauth2SignupProc",
            "/assets/",
            "/img/",
            "/js/",
            "/error"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 제외할 URL 패턴에 해당하는지 확인
        return excludeUrlPatterns.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal().toString())) {

            String username = null;

            if (authentication.getPrincipal() instanceof LocalAuthUser) {
                username = ((LocalAuthUser) authentication.getPrincipal()).getUsername();
            } else if (authentication.getPrincipal() instanceof OAuth2User) {
                username = ((OAuth2User) authentication.getPrincipal()).getName();
            } else if (authentication.getPrincipal() instanceof String) {
                username = (String) authentication.getPrincipal();
            }

            if (username != null) {
                String sessionId = request.getSession().getId();

                if (!sessionService.isSessionValid(username, sessionId)) {
                    SecurityContextHolder.clearContext();

                    // 세션 무효화 처리
                    request.getSession().invalidate();

                    // 로그인 페이지로 리다이렉션 - 필터체인 중단
                    response.sendRedirect("/auth/login?session=expired");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
