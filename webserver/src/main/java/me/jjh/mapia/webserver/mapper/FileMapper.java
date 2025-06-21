package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.board.BoardFileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : FileMapper
 * author         : JJH
 * date           : 2025-04-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-15        JJH       최초 생성
 */

@Mapper
public interface FileMapper {

    List<BoardFileDTO> getBoardFileListByIdx(Long boardIdx);
    BoardFileDTO getBoardFileByKey(String fileKey);

    void insertBoardFile(BoardFileDTO boardFileDTO);
}
