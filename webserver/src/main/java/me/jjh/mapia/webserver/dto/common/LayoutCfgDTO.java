package me.jjh.mapia.webserver.dto.common;

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
 * fileName       : LayoutConfigDTO
 * author         : JJH
 * date           : 2025-01-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-12        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class LayoutCfgDTO extends ComDTO {
    private Long layoutCfgIdx; // 레이아웃 idx
    private String layoutCfgPattern; // 적용할 url 패턴
    private String layoutCfgPath; // 레이아웃 경로
    private String isActive; // 사용 여부
}
