package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.board.*;
import me.jjh.mapia.webserver.dto.mail.MailDTO;
import me.jjh.mapia.webserver.dto.mail.MailTplDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : BoardMapper
 * author         : JJH
 * date           : 2025-01-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-13        JJH       최초 생성
 */

@Mapper
public interface BoardMapper {

    /* SELECT */
    int getBoardTotCount();
    List<BoardDTO> getBoardList(BoardSearchDTO boardSearchDTO);
    List<BoardTagDTO> getBoardTagList(BoardSearchDTO boardSearchDTO);
    BoardCtgDTO getBoardCtgByTagIdx(Long boardTagIdx);
    BoardDTO getBoardByIdx(Long boardIdx);
    List<BoardCmtDTO> getBoardCmtList(Long boardIdx);
    BoardCmtDTO getBoardCmtByIdx(Long boardCmtIdx);

    /* INSERT */
    void createBoard(BoardDTO boardDTO);
    void createCmtBoard(BoardCmtDTO boardCmtDTO);

    /* UPDATE */
    void updateBoardViewCount(Long boardIdx);
    /* DELETE */
}
