package me.jjh.mapia.webserver.service.role;

import me.jjh.mapia.webserver.dto.member.RoleDTO;
import org.apache.ibatis.annotations.Param;

/**
 * packageName    : me.jjh.mapia.service.role
 * fileName       : RoleService
 * author         : JJH
 * date           : 2025-03-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-11        JJH       최초 생성
 */
public interface RoleService {

    /**
     * 기본 설정된 권한 및 역할 리스트 조회(is_default = 'y')
     * @return 기본 권한 및 역할 리스트 반환
     */
    RoleDTO findDefaultRole();

    /**
     * 권한 및 역할 리스트를 조회(IDX 기반)
     * @param roleIdx
     * @return 일치하는 리스트 반환
     */
    RoleDTO findRoleListByIdx(@Param("roleIdx") Long roleIdx);

}
