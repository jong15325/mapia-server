package me.jjh.mapia.webserver.dto.department;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * packageName    : me.jjh.mapia.dto
 * fileName       : DeptDTO
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
public class DepartmentDTO extends ComDTO {
    private Long departmentIdx; // 부서 idx
    private String departmentNm; // 부서 이름
    private String departmentDesc; // 부서 설명
}
