package me.jjh.mapia.webserver.security.local.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RoleDTO;
import me.jjh.mapia.webserver.security.local.user.LocalAuthUser;
import me.jjh.mapia.webserver.service.member.LocalMemberService;
import me.jjh.mapia.webserver.service.role.RoleService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalAuthService implements UserDetailsService {

    private final RoleService roleService;

    private final LocalMemberService localMemberService;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        log.debug("[LOCAL AUTH SERVICE - loadUserByUsername] START");

        MemberDTO memberDTO = localMemberService.findMemberWithAuthById(memberId);

        // 멤버 체크
        if (DataVaildUtil.isObjectEmpty(memberDTO)) {
            log.warn("[LOCAL AUTH SERVICE - loadUserByUsername] DB에서 사용자 정보를 찾을 수 없습니다 memberID[{}]", memberId);
            throw new UsernameNotFoundException("loadUserByUsername - 사용자가 존재하지 않습니다");
        }

        RoleDTO role = roleService.findDefaultRole();

        // 계정 권한 및 역할 체크
        if (!DataVaildUtil.isNumberNull(memberDTO.getMemberRoleIdx())) {
            role = roleService.findRoleListByIdx(memberDTO.getMemberRoleIdx());
        }

        memberDTO.setRole(role);

        log.debug("[SERVICE - loadUserByUsername] DB - IDX [{}], 아이디 [{}]", memberDTO.getMemberIdx(), memberDTO.getMemberId());

        log.debug("[SERVICE - loadUserByUsername] END");

        return new LocalAuthUser(memberDTO);
    }
}
