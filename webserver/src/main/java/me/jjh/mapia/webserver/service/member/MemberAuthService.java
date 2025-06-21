package me.jjh.mapia.webserver.service.member;

import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import org.apache.ibatis.annotations.Param;

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
public interface MemberAuthService {

    /* SELECT */
    List<MemberAuthDTO> findMemberAuthByMemberIdx(Long memberIdx);
    MemberAuthDTO findMemberAuthByProvider(String memberAuthProvider, String memberAuthProviderId);

    /* INSERT */
    void insertMemberAuth(MemberAuthDTO memberAuthDTO);

    /* UPDATE */
    /* DELETE */
}
