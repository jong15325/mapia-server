package me.jjh.mapia.webserver.service.file.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.board.BoardFileDTO;
import me.jjh.mapia.webserver.mapper.BoardMapper;
import me.jjh.mapia.webserver.mapper.FileMapper;
import me.jjh.mapia.webserver.service.file.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.service.file.impl
 * fileName       : FileServiceImpl
 * author         : JJH
 * date           : 2025-04-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-15        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Override
    public List<BoardFileDTO> getBoardFileListByIdx(Long boardIdx) {
        log.debug("[BOARD SERVICE - getBoardFileListByIdx] START");

        List<BoardFileDTO> boardFileDTO = fileMapper.getBoardFileListByIdx(boardIdx);

        log.debug("[BOARD SERVICE - getBoardFileListByIdx] END");

        return boardFileDTO;
    }

    @Override
    public BoardFileDTO getBoardFileByKey(String fileKey) {
        log.debug("[BOARD SERVICE - getBoardFileByKey] START");

        BoardFileDTO boardFileDTO = fileMapper.getBoardFileByKey(fileKey);

        log.debug("[BOARD SERVICE - getBoardFileByKey] END");

        return boardFileDTO;
    }

    @Override
    public void insertBoardFile(BoardFileDTO boardFileDTO) {
        log.debug("[BOARD SERVICE - insertBoardFile] START");

        fileMapper.insertBoardFile(boardFileDTO);

        log.debug("[BOARD SERVICE - insertBoardFile] END");
    }
}
