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
 * fileName       : BoardTagDTO
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
public class BoardTagDTO extends ComDTO {
    private Long boardTagIdx; // idx
    private Long boardCtgIdx; // 카테고리 이름
    private String boardTagNm; // 태그 이름
    private String boardTagDesc; //태그 설명
    private String boardTagColor; // 태그 컬러
    private String isActive; // 활성화 여부
    private String isDeleted; // 삭제 여부
}
