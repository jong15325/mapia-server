package me.jjh.mapia.webserver.service.file;

import me.jjh.mapia.webserver.dto.board.*;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.service.file
 * fileName       : FileService
 * author         : JJH
 * date           : 2025-04-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-15        JJH       최초 생성
 */
public interface FileService {

    /* SELECT */

    /**
     * 게시글 파일 리스트 조회 By idx
     * @param boardIdx
     * @return
     */
    List<BoardFileDTO> getBoardFileListByIdx(Long boardIdx);

    /**
     * 게시글 파일 조회 By key
     * @param fileKey
     * @return
     */
    BoardFileDTO getBoardFileByKey(String fileKey);

    /* INSERT */
    /**
     * 단일 파일 등록
     * @param boardFileDTO
     */
    void insertBoardFile(BoardFileDTO boardFileDTO);

    /* UPDATE */
    /* DELETE */
}
