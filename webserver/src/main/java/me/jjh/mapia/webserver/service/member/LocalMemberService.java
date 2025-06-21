package me.jjh.mapia.webserver.service.member;

import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import me.jjh.mapia.webserver.response.member.MemberResDTO;

/**
 *packageName    : me.jjh.mapia.service.member.local
 * fileName       : LocalMemberService
 * author         : JJH
 * date           : 2025-02-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-21        JJH       최초 생성
 */
public interface LocalMemberService {

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
     * 멤버 id로 member 및 memberAuth 조회
     * @param memberId
     * @return
     */
    MemberDTO findMemberWithAuthById(String memberId);

    /**
     * 새로운 멤버 정보를 데이터베이스에 등록
     *
     * @param memberDTO 데이터베이스에 삽입할 멤버 정보를 담은 Member 객체
     * @return 
     */
    void insertMember(MemberDTO memberDTO);

    /**
     * 멤버 정보 업데이트
     * @param memberDTO
     */
    void updateMember(MemberDTO memberDTO);

    /**
     * 회원가입 인증 코드 발송
     * @param memberId
     * @param memberPwd
     * @return
     */
    VerifyDTO signupVerifySend(String memberId, String memberPwd);

    /**
     * 회원가입 인증 코드 재발송
     * @return
     */
    VerifyDTO signupVerifyReSend(String token);

}
