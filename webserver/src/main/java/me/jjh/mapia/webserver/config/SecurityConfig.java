package me.jjh.mapia.webserver.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.security.RedisSessionValidationFilter;
import me.jjh.mapia.webserver.security.local.handler.LocalAuthFailHandler;
import me.jjh.mapia.webserver.security.local.handler.LocalAuthSuccessHandler;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.security.oauth2.config.OAuth2ClientRegRepository;
import me.jjh.mapia.webserver.security.oauth2.handler.OAuth2FailureHandler;
import me.jjh.mapia.webserver.security.oauth2.handler.OAuth2SuccessHandler;
import me.jjh.mapia.webserver.security.oauth2.service.OAuth2ClientService;
import me.jjh.mapia.webserver.security.oauth2.service.OAuth2Service;
import me.jjh.mapia.webserver.security.oauth2.user.OAuth2User;
import me.jjh.mapia.webserver.service.session.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity //모든 요청이 security를 통하도록
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2ClientRegRepository OAuth2ClientRegRepository;
    private final JdbcTemplate jdbcTemplate;
    private final LocalAuthFailHandler localAuthFailHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2Service OAuth2Service;
    private final OAuth2ClientService OAuth2ClientService;
    private final SessionService sessionService;
    private final SessionRegistry sessionRegistry;
    private final LocalAuthSuccessHandler localAuthSuccessHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll() //요청을 다른 서블릿이나 jsp로 전달하는 경우 사용 / 내부요청은 허용 외부요청은 비허용

                        /* 정적 리소스 허용*/
                        .requestMatchers( "/assets/**", "/img/**", "/js/**").permitAll()

                        /* 페이지 허용 */
                        .requestMatchers("/", "/error/**",
                                "/auth/login", "/auth/signup", "/auth/signupProc", "/auth/signupVerify",
                                "/auth/signupVerifyResend", "/auth/signupVerifyProc", "/auth/oauth2SignupProc",
                                "/clearAlert").permitAll()

                        /* jsp 직접 접근 차단*/
                        .requestMatchers("/WEB-INF/**").denyAll()

                        /* 관리자 접근 페이지 */
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        //.loginProcessingUrl("/loginProc")    // POST 요청 (login 창에 입력한 데이터를 처리)
                        .usernameParameter("memberId")	// login에 필요한 id 값을 email로 설정 (default는 username)
                        .passwordParameter("memberPwd")	// login에 필요한 password 값을 password(default)로 설정
                        .successHandler(localAuthSuccessHandler)
                        .failureHandler(localAuthFailHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .clientRegistrationRepository(OAuth2ClientRegRepository.getClientRegistrationRepository())
                        .authorizedClientService(OAuth2ClientService.oAuth2AuthorizedClientService(jdbcTemplate, OAuth2ClientRegRepository.getClientRegistrationRepository()))
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig.userService(OAuth2Service)))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)  //세션 무효화
                        .clearAuthentication(true)    // 인증 정보 제거
                        .deleteCookies("JSESSIONID", "SESSION") // "SESSION" 쿠키 추가 (Spring Session Redis)
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                String username = null;

                                if (authentication.getPrincipal() instanceof LocalAuthUser localUser) {
                                    username = localUser.getUsername();
                                } else if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
                                    username = oauthUser.getName();
                                } else if (authentication.getPrincipal() instanceof String) {
                                    username = (String) authentication.getPrincipal();
                                }

                                if (username != null && !username.equals("anonymousUser")) {
                                    // 모든 세션이 아닌 현재 세션만 로그아웃
                                    String sessionId = request.getSession().getId();
                                    sessionService.logout(username, sessionId);
                                }
                            }
                        })
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) // 동시접속 1
                        .maxSessionsPreventsLogin(false) //동시 로그인 차단, false인 경우 기존 세션 만료(default)
                        .expiredUrl("/auth/login")
                )
                .addFilterBefore(redisSessionValidationFilter(), UsernamePasswordAuthenticationFilter.class)

        ;
        return http.build();
    }

    @Bean // 비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // http 세션과 sessionRegistry와 동기화
    public HttpSessionEventPublisher httpSessionEventPublisher() { return new HttpSessionEventPublisher(); }

    @Bean
    public RedisSessionValidationFilter redisSessionValidationFilter() {
        return new RedisSessionValidationFilter(sessionService);
    }
}
