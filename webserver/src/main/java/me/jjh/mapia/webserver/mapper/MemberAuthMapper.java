package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : MemberAuthMapper
 * author         : JJH
 * date           : 2025-03-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-23        JJH       최초 생성
 */

@Mapper
public interface MemberAuthMapper {

    /* SELECT */
    List<MemberAuthDTO> findMemberAuthByMemberIdx(@Param("memberIdx") Long memberIdx);
    MemberAuthDTO findMemberAuthByProvider(@Param("memberAuthProvider") String memberAuthProvider,
                                           @Param("memberAuthProviderId") String memberAuthProviderId);

    /* INSERT */
    void insertMemberAuth(MemberAuthDTO memberAuthDTO);

    /* UPDATE */
    /* DELETE */


}
