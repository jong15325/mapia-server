package me.jjh.mapia.webserver.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;
import me.jjh.mapia.webserver.dto.member.MemberFileDTO;
import me.jjh.mapia.webserver.util.DataVaildUtil;

/**
 * packageName    : me.jjh.mapia.dto.board
 * fileName       : BoardCmtDTO
 * author         : JJH
 * date           : 2025-04-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-16        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class BoardCmtDTO extends ComDTO {

    private Long boardCmtIdx;       // 댓글 idx
    private Long boardIdx;          // 게시글 idx
    private Long rootCmtIdx;        // 최상위 부모 댓글
    private Long parentCmtIdx;      // 상위 댓글 idx (NULL이면 최상위 댓글)
    private Long boardCmtToIdx;     // 답글 대상 유저 idx
    private String boardCmtToId;    // 답글 대상 유저 id
    private String boardCmtCont;    // 댓글 내용
    private String isDeleted;       // 삭제 여부
    private String replyCont; // 대댓글

    //JOIN
    private String memberProfilePath; // 회원 프로필 사진

    public String getHtmlCont() {
        return DataVaildUtil.getSafeHtml(this.boardCmtCont);
    }
}
