package me.jjh.mapia.webserver.security.local.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.security.local.service.LocalAuthService;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.service.session.SessionService;
import me.jjh.mapia.webserver.util.SecurityUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocalAuthProvider implements AuthenticationProvider {

    private final LocalAuthService localAuthService;

    private final PasswordEncoder passwordEncoder;  // BCryptPasswordEncoder 사용, securityConfig

    private final SessionService sessionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("[LOCAL AUTH PROVIDER - authenticate] START");

        String memberId = authentication.getName();
        String mPassword = (String) authentication.getCredentials();

        log.debug("[LOCAL AUTH PROVIDER - authenticate] 입력된 아이디 [{}], 비밀번호 [{}]", memberId, SecurityUtil.encryptPassword(mPassword));

        // UserDetailsService를 통해 사용자 정보 가져오기
        LocalAuthUser member = (LocalAuthUser) localAuthService.loadUserByUsername(memberId);

        if (member != null) {
            if (!passwordEncoder.matches(mPassword, member.getPassword())) {
                log.warn("[LOCAL AUTH PROVIDER - authenticate] 입력한 비밀번호가 일치하지 않습니다 memberId[{}]", memberId);
                throw new BadCredentialsException("authenticate - 비밀번호 미일치가 일치하지 않습니다");
            }
            
            if (!member.isEnabled()) {
                log.warn("[LOCAL AUTH SERVICE - loadUserByUsername] 비활성화 계정입니다 memberId[{}]", memberId);
                throw new DisabledException("loadUserByUsername - 비활성화 계정입니다");
            }
        } else {
            log.warn("[LOCAL AUTH PROVIDER - authenticate] DB에서 사용자 정보를 찾을 수 없습니다 memberId[{}]", memberId);
            throw new UsernameNotFoundException("loadUserByUsername - 해당 사용자가 존재하지 않습니다");
        }

        log.debug("[LOCAL AUTH PROVIDER - authenticate] END");

        // 인증 성공 시 인증된 토큰 반환
        return new UsernamePasswordAuthenticationToken(member, mPassword, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
