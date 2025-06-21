package me.jjh.mapia.webserver.dto.board;

import lombok.*;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BoardDTO extends ComDTO {
    private Long boardIdx; // 게시글 idx
    private String boardTitle; // 제목
    private String boardCont; // 내용
    private Long boardViewCnt; // 조회수
    private Integer boardCmtCnt; // 댓글수
    private Integer boardRecomCnt; // 추천수
    private String cmtIsAllow; // 댓글 허용 여부
    private String isActive; // 활성화 여부
    private String isDeleted; // 삭제 여부

    //JOIN
    private BoardCtgDTO boardCtg = new BoardCtgDTO(); // 게시판 카테고리
    private BoardTagDTO boardTag = new BoardTagDTO(); // 게시판 태그
}
