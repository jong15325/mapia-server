package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : BoardMapper
 * author         : JJH
 * date           : 2025-01-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-13        JJH       최초 생성
 */

@Mapper
public interface MemberMapper {

    /* SELECT */
    Integer countMemberById(@Param("memberId") String memberId);
    MemberDTO findMemberByIdx(@Param("memberIdx") Long memberIdx);
    MemberDTO findMemberById(@Param("memberId") String memberId);
    MemberDTO findOauth2MemberInfo(@Param("memberId") String memberId,
                          @Param("memberProv") String memberProv,
                          @Param("memberProvId") String memberProvId);

    /* INSERT */
    void insertMember(MemberDTO memberDTO);


    /* UPDATE */
    void updateMember(MemberDTO memberDTO);
    void updateLastLogin(@Param("memberIdx") Long memberIdx);

    /* DELETE */

}
