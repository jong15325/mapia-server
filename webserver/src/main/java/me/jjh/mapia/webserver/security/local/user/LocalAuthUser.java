package me.jjh.mapia.webserver.security.local.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import me.jjh.mapia.webserver.util.RoleUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class LocalAuthUser implements UserDetails, Serializable {

    private final MemberDTO member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleUtil.getAuthorities(member);
    }

    @Override
    public String getPassword() {
        if (!DataVaildUtil.isListEmpty(member.getMemberAuths())) {
            for (MemberAuthDTO auth : member.getMemberAuths()) {
                if ("local".equals(auth.getMemberAuthProvider())) {
                    log.debug("auth.getMemberAuthPassword()auth.getMemberAuthPassword() : " + auth.getMemberAuthPassword());
                    return auth.getMemberAuthPassword();
                }
            }
        }

        return null;
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return "Y".equals(member.getIsActive());
    }

    //중복로그인 체크와 같은 기능 사용시 비교대상을 custom으로 재정의 해줘야함
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalAuthUser) {
            return this.member.getMemberId().equals(((LocalAuthUser) obj).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.member.getMemberId().hashCode();
    }

    public MemberResDTO getMemberResDTO() {
        return MemberResDTO.fromDTO(member);
    }
}
