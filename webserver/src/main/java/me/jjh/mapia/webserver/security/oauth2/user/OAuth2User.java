package me.jjh.mapia.webserver.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.ComDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.util.RoleUtil;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User, Serializable  {

    @Serial
    private static final long serialVersionUID = 77093923L;

    private final MemberDTO member;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleUtil.getAuthorities(member);
    }

    @Override
    public String getName() {
        return member.getMemberId();
    }

    public MemberResDTO getMemberResDTO() {
        return MemberResDTO.fromDTO(member);
    }
}
