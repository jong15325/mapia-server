package me.jjh.mapia.webserver.service.board;

import com.github.pagehelper.PageInfo;
import me.jjh.mapia.webserver.dto.board.*;

import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.service
 * fileName       : BoardService
 * author         : JJH
 * date           : 2025-01-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-12        JJH       최초 생성
 */

public interface BoardService {

    /**
     * 게시판 리스트
     * @param boardSearchDTO
     * @return
     */
    PageInfo<BoardDTO> getBoardList(BoardSearchDTO boardSearchDTO);

    /**
     * 게시판 태그 리스트
     * @param boardSearchDTO
     * @return
     */
    List<BoardTagDTO> getBoardTagList(BoardSearchDTO boardSearchDTO);

    /**
     * 게시판 카테고리 조회
     * @param boardTagIdx
     * @return
     */
    BoardCtgDTO getBoardCtgByTagIdx(Long boardTagIdx);

    /**
     * 게시글 등록
     * @param boardDTO
     * @return 등록된 게시글 ID
     */
    Long createBoard(BoardDTO boardDTO);

    /**
     * 게시글 조회 By id
     * @param boardIdx
     * @return 등록된 게시글 조회
     */
    Map<String, Object> getBoardByIdx(Long boardIdx);

    /**
     * 게시글 view 카운트 update
     * @param boardIdx
     */
    void updateBoardViewCount(Long boardIdx);

    /**
     * 게시글 댓글 리스트 조회 by idx
     * @param boardIdx
     * @return List<BoardCmtDTO>
     */
    List<BoardCmtDTO> getBoardCmtList(Long boardIdx);

    /**
     * 게시글 댓글 등록
     * @param boardCmtDTO
     * @return BoardCmtDTO
     */
    BoardCmtDTO createCmtBoard(BoardCmtDTO boardCmtDTO);

    /**
     * 등록된 댓글 조회 by idx
     * @param boardCmtIdx
     * @return
     */
    BoardCmtDTO getBoardCmtByIdx(Long boardCmtIdx);

}
