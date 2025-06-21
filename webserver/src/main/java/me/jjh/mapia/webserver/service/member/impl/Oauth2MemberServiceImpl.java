package me.jjh.mapia.webserver.service.member.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RoleDTO;
import me.jjh.mapia.webserver.mapper.MemberAuthMapper;
import me.jjh.mapia.webserver.mapper.MemberMapper;
import me.jjh.mapia.webserver.service.member.Oauth2MemberService;
import me.jjh.mapia.webserver.service.role.RoleService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import me.jjh.mapia.webserver.util.SecurityUtil;
import me.jjh.mapia.webserver.util.UserAgentUtil;
import org.springframework.stereotype.Service;

/**
 * packageName    : me.jjh.mapia.service.impl
 * fileName       : Oauth2MemberServiceImpl
 * author         : JJH
 * date           : 2025-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class Oauth2MemberServiceImpl implements Oauth2MemberService {

    private final MemberMapper memberMapper;

    private final RoleService roleService;

    private final MemberAuthMapper memberAuthMapper;

    @Override
    public MemberDTO findMemberByIdx(Long memberIdx) {

        log.debug("[OAUTH2 MEMBER SERVICE - findMemberByIdx] START");

        log.debug("[OAUTH2 MEMBER SERVICE - findMemberByIdx] END");

        return memberMapper.findMemberByIdx(memberIdx);
    }

    @Override
    public MemberDTO findMemberById(String memberId) {

        log.debug("[OAUTH2 MEMBER SERVICE - findMemberById] START");

        log.debug("[OAUTH2 MEMBER SERVICE - findMemberById] END");

        return memberMapper.findMemberById(memberId);
    }

    @Override
    public MemberDTO findAndUpdateMemberByIdx(Long memberIdx) {
        log.debug("[OAUTH2 MEMBER SERVICE - findAndUpdateMemberByIdx] START");

        MemberDTO memberDTO = memberMapper.findMemberByIdx(memberIdx);

        if (DataVaildUtil.isObjectEmpty(memberDTO)) {
            memberMapper.updateLastLogin(memberIdx);
        }

        log.debug("[OAUTH2 MEMBER SERVICE - findAndUpdateMemberByIdx] END");

        return memberDTO;
    }

    @Override
    public MemberDTO createOauth2Member(String oauth2Email, String oauth2Provider, String oauth2ProviderId) {
        log.debug("[OAUTH2 MEMBER SERVICE - createOauth2Member] START");

        RoleDTO roleDTO = roleService.findDefaultRole();

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(oauth2Email);
        memberDTO.setMemberLoginIp(UserAgentUtil.getClientIp(null));
        memberDTO.setMemberRoleIdx(roleDTO.getRoleIdx());
        memberDTO.setIsActive("Y");
        memberDTO.setCreatedIdx(0L);
        memberDTO.setCreatedId("SYSTEM");
        memberMapper.insertMember(memberDTO);

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO();
        memberAuthDTO.setMemberIdx(memberDTO.getMemberIdx());
        memberAuthDTO.setMemberAuthProvider(oauth2Provider);
        memberAuthDTO.setMemberAuthProviderId(oauth2ProviderId);
        memberAuthMapper.insertMemberAuth(memberAuthDTO);

        log.debug("[OAUTH2 MEMBER SERVICE - createOauth2Member] END");

        return memberDTO;
    }

    @Override
    public MemberDTO updateOauth2Member(MemberDTO memberDTO, boolean isLastLogin) {
        log.debug("[OAUTH2 MEMBER SERVICE - updateOauth2Member] START");

        memberMapper.updateMember(memberDTO);

        log.debug("[OAUTH2 MEMBER SERVICE - updateOauth2Member] END");

        return memberDTO;
    }
}
