package me.jjh.mapia.webserver.service.member;

import me.jjh.mapia.webserver.dto.member.MemberDTO;

/**
 * packageName    : me.jjh.mapia.service
 * fileName       : Ouath2MemberService
 * author         : JJH
 * date           : 2025-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        JJH       최초 생성
 */
public interface Oauth2MemberService {

    /**
     * 멤버 idx로 멤버 조회
     * @param memberIdx
     * @return
     */
    MemberDTO findMemberByIdx(Long memberIdx);

    /**
     * 멤버 id로 멤버 조회
     * @param memberId
     * @return
     */
    MemberDTO findMemberById(String memberId);

    /**
     * Oauth2 로그인 멤버 조회 후 업데이트
     * @param memberIdx
     */
    MemberDTO findAndUpdateMemberByIdx(Long memberIdx);

    /**
     * Oauth2 로그인 멤버 생성
     * @param oauth2Email
     * @param oauth2Provider
     * @param oauth2ProviderId
     * @return
     */
    MemberDTO createOauth2Member(String oauth2Email, String oauth2Provider, String oauth2ProviderId);


    /**
     * Oauth2 로그인 멤버 수정
     * @param memberDTO
     * @param isLastLogin
     * @return
     */
    MemberDTO updateOauth2Member(MemberDTO memberDTO, boolean isLastLogin);

}
