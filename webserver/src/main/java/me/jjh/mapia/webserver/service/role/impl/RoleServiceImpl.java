package me.jjh.mapia.webserver.service.role.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.member.RoleDTO;
import me.jjh.mapia.webserver.mapper.RoleMapper;
import me.jjh.mapia.webserver.service.role.RoleService;
import org.springframework.stereotype.Service;

/**
 * packageName    : me.jjh.mapia.service.role.impl
 * fileName       : RoleServiceImpl
 * author         : JJH
 * date           : 2025-03-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-11        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public RoleDTO findDefaultRole() {
        log.debug("[ROLE SERVICE - findDefaultRole] START");

        log.debug("[ROLE SERVICE - findDefaultRole] END");

        return roleMapper.findDefaultRole();
    }

    @Override
    public RoleDTO findRoleListByIdx(Long roleIdx) {
        log.debug("[ROLE SERVICE - findRoleListByIdx] START");

        log.debug("[ROLE SERVICE - findRoleListByIdx] END");

        return roleMapper.findRoleListByIdx(roleIdx);
    }
}
