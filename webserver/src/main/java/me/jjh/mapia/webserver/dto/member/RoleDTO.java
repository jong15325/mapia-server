package me.jjh.mapia.webserver.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * packageName    : me.jjh.mapia.dto
 * fileName       : RoleDTO
 * author         : JJH
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class RoleDTO extends ComDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 602049393L;

    private Long roleIdx; // idx
    private String roleNm; // 역할 이름
    private String roleDesc; // 역할 설명
    private String isDefault; // 기본 여부

    private List<RolePermDTO> rolePerms;
}
