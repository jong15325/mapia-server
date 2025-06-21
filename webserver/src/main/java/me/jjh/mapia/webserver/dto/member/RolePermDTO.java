package me.jjh.mapia.webserver.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * packageName    : me.jjh.mapia.dto
 * fileName       : PermissionDTO
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
public class RolePermDTO extends ComDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 52938483L;

    private Long rolePermIdx; // idx
    private String rolePermNm; // 역할 이름
    private String rolePermDesc; // 역할 설명
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 생성일
    private Long createdIdx; // 생성자 idx
    private String createdId; // 생성자 id
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일
    private Long updatedIdx; // 수정자 idx
    private String updatedId; // 수정자 id

}
