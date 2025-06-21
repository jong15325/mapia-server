package me.jjh.mapia.webserver.util;

import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RolePermDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : AuthUtil
 * author         : JJH
 * date           : 2025-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        JJH       최초 생성
 */
public class RoleUtil {

    /**
     * 멤버 권한 및 역할 반환
     * @param member
     * @return
     */
    public static Collection<? extends GrantedAuthority> getAuthorities(MemberDTO member) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (DataVaildUtil.isObjectEmpty(member) || DataVaildUtil.isObjectEmpty(member.getRole())) {
            return authorities;
        }

        // 역할(ROLE)
        authorities.add(new SimpleGrantedAuthority(member.getRole().getRoleNm()));

        // 권한(PERMISSION)
        List<RolePermDTO> rolePerms = member.getRole().getRolePerms();

        if (!CollectionUtils.isEmpty(rolePerms)) {
            authorities.addAll(member.getRole().getRolePerms().stream()
                    .map(rolePerm -> new SimpleGrantedAuthority(rolePerm.getRolePermNm()))
                    .toList());
        }

        return authorities;
    }
}
