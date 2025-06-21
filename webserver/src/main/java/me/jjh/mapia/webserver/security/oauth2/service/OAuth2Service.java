package me.jjh.mapia.webserver.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.exception.OAuth2LinkReqException;
import me.jjh.mapia.webserver.common.exception.OAuth2SignupReqException;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RoleDTO;
import me.jjh.mapia.webserver.mapper.MemberAuthMapper;
import me.jjh.mapia.webserver.security.oauth2.user.OAuth2User;
import me.jjh.mapia.webserver.security.oauth2.response.KakaoResponse;
import me.jjh.mapia.webserver.security.oauth2.response.NaverResponse;
import me.jjh.mapia.webserver.security.oauth2.response.OAuth2Response;
import me.jjh.mapia.webserver.service.member.Oauth2MemberService;
import me.jjh.mapia.webserver.service.role.RoleService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service

public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberAuthMapper memberAuthMapper;

    private final Oauth2MemberService oauth2MemberService;

    private final RoleService roleService;

    @Transactional // 동시요청 방지
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("[OAUTH2 SERVICE  - loadUser] START");

        org.springframework.security.oauth2.core.user.OAuth2User oAuth2User = super.loadUser(userRequest);

        log.debug("[OAUTH2 SERVICE  - loadUser] oAuth2User [{}]", oAuth2User);

        //어떤 인증 인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        log.debug("[OAUTH2 SERVICE  - loadUser] registrationId [{}]", registrationId);

        OAuth2Response oAuth2response = null;

        if(registrationId.equals("naver")) {
            oAuth2response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            log.warn("[OAUTH2 SERVICE  - loadUser] 지원하지 않는 플랫폼입니다 [{}]", registrationId);
            throw new OAuth2AuthenticationException("지원하지 않는 플랫폼입니다");
        }

        log.debug("[OAUTH2 SERVICE] - [{}] Id[{}] Name[{}] Email[{}]", oAuth2response.getProvider(), oAuth2response.getProviderId(),
                oAuth2response.getName(), oAuth2response.getEmail());

        // 소셜 로그인 정보로 기존에 등록된 인증 정보 확인
        MemberAuthDTO memberAuthDTO = memberAuthMapper.findMemberAuthByProvider(registrationId, oAuth2response.getProviderId());

        // 기존 인증 정보가 있는 경우 업데이트 후 로그인 진행
        if(!DataVaildUtil.isObjectEmpty(memberAuthDTO)) {
            MemberDTO member = oauth2MemberService.findAndUpdateMemberByIdx(memberAuthDTO.getMemberIdx());


            RoleDTO role = roleService.findDefaultRole();

            // 계정 권한 및 역할 체크
            if (!DataVaildUtil.isNumberNull(member.getMemberRoleIdx())) {
                role = roleService.findRoleListByIdx(member.getMemberRoleIdx());
            }

            member.setRole(role);

            log.debug("[OAUTH2 SERVICE  - loadUser] END");

            return new OAuth2User(member);
        } else {
            // 인증 수단 중 이메일로는 고유 체크를 할 수 없음
            //MemberDTO member = oauth2MemberService.createOauth2Member(oAuth2response.getEmail(), oAuth2response.getProvider(), oAuth2response.getProviderId());
            throw new OAuth2SignupReqException("소셜 이메일로 등록된 회원 정보가 없습니다, 회원가입이 필요합니다", oAuth2response);
            /*MemberDTO member = oauth2MemberService.findMemberById(oAuth2response.getEmail());

            // 이메일이 일치하는 회원이 있을 경우 - 로그인 화면으로 이동
            if(!DataVaildUtil.isObjectEmpty(member)) {
                throw new OAuth2LinkReqException("소셜 이메일로 등록된 회원 정보가 존재합니다", oAuth2response);
            }

            // 이메일이 일치하는 회원이 없을 경우 - 회원가입 화면으로 이동
            throw new OAuth2SignupReqException("소셜 이메일로 등록된 회원 정보가 없습니다, 회원가입이 필요합니다", oAuth2response);*/

            //return new OAuth2User(member);
        }
    }
}
