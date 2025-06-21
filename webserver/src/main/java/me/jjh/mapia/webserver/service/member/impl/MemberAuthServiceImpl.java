package me.jjh.mapia.webserver.service.member.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.mapper.MemberAuthMapper;
import me.jjh.mapia.webserver.service.member.MemberAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.service.member
 * fileName       : MemberAuthService
 * author         : JJH
 * date           : 2025-03-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-24        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberAuthMapper memberAuthMapper;

    @Override
    public List<MemberAuthDTO> findMemberAuthByMemberIdx(Long memberIdx) {

        log.debug("[MEMBER AUTH SERVICE - findMemberAuthByIdx] START");

        List<MemberAuthDTO> authList = memberAuthMapper.findMemberAuthByMemberIdx(memberIdx);

        log.debug("[MEMBER AUTH SERVICE - findMemberAuthByIdx] END");

        return authList;
    }

    @Override
    public MemberAuthDTO findMemberAuthByProvider(String memberAuthProvider, String memberAuthProviderId) {

        log.debug("[MEMBER AUTH SERVICE - findMemberAuthByProvider] START");

        log.debug("[MEMBER AUTH SERVICE - findMemberAuthByProvider] END");

        return memberAuthMapper.findMemberAuthByProvider(memberAuthProvider, memberAuthProviderId);
    }

    @Override
    public void insertMemberAuth(MemberAuthDTO memberAuthDTO) {

        log.debug("[MEMBER AUTH SERVICE - insertMemberAuth] START");

        memberAuthMapper.insertMemberAuth(memberAuthDTO);

        log.debug("[MEMBER AUTH SERVICE - insertMemberAuth] END");

    }
}
