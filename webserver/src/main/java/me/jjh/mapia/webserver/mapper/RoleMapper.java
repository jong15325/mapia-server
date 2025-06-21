package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.member.RoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : RoleMapper
 * author         : JJH
 * date           : 2025-02-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-10        JJH       최초 생성
 */

@Mapper
public interface RoleMapper {

    /* SELECT */
    RoleDTO findRoleListByIdx(@Param("roleIdx") Long roleIdx);
    RoleDTO findDefaultRole();

    /* INSERT */
    /* UPDATE */
    /* DELETE */


}
