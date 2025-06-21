package me.jjh.mapia.webserver.dto.board;

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
 * fileName       : boardCtgDTO
 * author         : JJH
 * date           : 2025-01-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-13        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class BoardCtgDTO extends ComDTO {
    private Long boardCtgIdx; // idx
    private String boardCtgType; // 카테고리 타입
    private String boardCtgNm; // 카테고리 이름
    private String boardCtgDesc; // 카테고리 설명
    private String isActive; // 활성화 여부
    private String isDeleted; // 삭제 여부
}
