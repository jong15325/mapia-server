package me.jjh.mapia.webserver.service.board.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.board.*;
import me.jjh.mapia.webserver.mapper.BoardMapper;
import me.jjh.mapia.webserver.service.board.BoardService;
import me.jjh.mapia.webserver.service.file.FileService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *packageName    : me.jjh.mapia.service.impl
 * fileName       : BoardServiceImpl
 * author         : JJH
 * date           : 2025-01-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-12        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final FileService fileService;

    @Override
    public PageInfo<BoardDTO> getBoardList(BoardSearchDTO boardSearchDTO) {
        log.debug("[BOARD SERVICE - getBoardList] STAR");

        PageHelper.startPage(boardSearchDTO.getPageNum(), boardSearchDTO.getPageSize());

        List<BoardDTO> boardList = boardMapper.getBoardList(boardSearchDTO);

        log.debug("[BOARD SERVICE - getBoardList] END");
        return new PageInfo<>(boardList, boardSearchDTO.getNavigatePages());
    }

    @Override
    public List<BoardTagDTO> getBoardTagList(BoardSearchDTO boardSearchDTO) {
        log.debug("[BOARD SERVICE - getBoardTagList] START");

        List<BoardTagDTO> list = boardMapper.getBoardTagList(boardSearchDTO);

        log.debug("[BOARD SERVICE - getBoardTagList] END");

        return list;
    }

    @Override
    public BoardCtgDTO getBoardCtgByTagIdx(Long boardTagIdx) {
        log.debug("[BOARD SERVICE - getBoardCtgByTagIdx] STAR");

        BoardCtgDTO boardCtgDTO = boardMapper.getBoardCtgByTagIdx(boardTagIdx);

        log.debug("[BOARD SERVICE - getBoardCtgByTagIdx] END");

        return boardCtgDTO;
    }

    @Override
    public Long createBoard(BoardDTO boardDTO) {
        log.debug("[BOARD SERVICE - createBoard] START");

        // 태그 정보로 카테고리 정보 조회
        if (boardDTO.getBoardTag() != null && boardDTO.getBoardTag().getBoardTagIdx() != null) {
            Long boardTagIdx = boardDTO.getBoardTag().getBoardTagIdx();

            // 카테고리 정보 조회
            BoardCtgDTO boardCtgDTO = boardMapper.getBoardCtgByTagIdx(boardTagIdx);
            if (boardCtgDTO != null) {
                boardDTO.setBoardCtg(boardCtgDTO);
            } else {
                log.warn("태그 ID에 해당하는 카테고리를 찾을 수 없습니다: {}", boardTagIdx);
            }
        }

        boardMapper.createBoard(boardDTO);
        Long boardIdx = boardDTO.getBoardIdx();

        log.debug("[BOARD SERVICE - createBoard] END");

        return boardIdx;
    }

    @Override
    public Map<String, Object> getBoardByIdx(Long boardIdx) {
        log.debug("[BOARD SERVICE - getBoardByIdx] START");
        Map<String, Object> resultMap = new HashMap<>();

        BoardDTO boardDTO = boardMapper.getBoardByIdx(boardIdx);

        if(DataVaildUtil.isObjectEmpty(boardDTO)) {
            return null;
        }

        // 게시글 존재 시 파일
        List<BoardFileDTO> fileList = fileService.getBoardFileListByIdx(boardIdx);

        List<BoardCmtDTO> cmtList = getBoardCmtList(boardIdx);

        resultMap.put("boardDTO", boardDTO);
        resultMap.put("fileList", fileList);
        resultMap.put("cmtList", cmtList);

        log.debug("[BOARD SERVICE - getBoardByIdx] END");

        return resultMap;
    }

    @Override
    public void updateBoardViewCount(Long boardIdx) {
        log.debug("[BOARD SERVICE - updateBoardViewCount] START");

        boardMapper.updateBoardViewCount(boardIdx);

        log.debug("[BOARD SERVICE - updateBoardViewCount] END");
    }

    @Override
    public List<BoardCmtDTO> getBoardCmtList(Long boardIdx) {
        log.debug("[BOARD SERVICE - getBoardCmtList] START");

        List<BoardCmtDTO> cmtList = boardMapper.getBoardCmtList(boardIdx);

        log.debug("[BOARD SERVICE - getBoardCmtList] END");

        return cmtList;
    }

    @Override
    public BoardCmtDTO createCmtBoard(BoardCmtDTO boardCmtDTO) {
        log.debug("[BOARD SERVICE - createCmtBoard] START");

        if(!DataVaildUtil.isLongEmpty(boardCmtDTO.getParentCmtIdx())) {
            BoardCmtDTO parentCmt = getBoardCmtByIdx(boardCmtDTO.getParentCmtIdx());
            boardCmtDTO.setBoardIdx(parentCmt.getBoardIdx());
            if(!DataVaildUtil.isLongEmpty(parentCmt.getRootCmtIdx())) {
                boardCmtDTO.setRootCmtIdx(parentCmt.getRootCmtIdx());
            }else {
                boardCmtDTO.setRootCmtIdx(boardCmtDTO.getParentCmtIdx());
            }

            boardCmtDTO.setBoardCmtToIdx(parentCmt.getCreatedIdx());
            boardCmtDTO.setBoardCmtToId(parentCmt.getCreatedId());
        }

        boardMapper.createCmtBoard(boardCmtDTO);

        BoardCmtDTO savedCmt = getBoardCmtByIdx(boardCmtDTO.getBoardCmtIdx());

        log.debug("[BOARD SERVICE - createCmtBoard] END");

        return savedCmt;
    }

    @Override
    public BoardCmtDTO getBoardCmtByIdx(Long boardCmtIdx) {
        log.debug("[BOARD SERVICE - getBoardCmtByIdx] START");

        BoardCmtDTO cmt = boardMapper.getBoardCmtByIdx(boardCmtIdx);

        log.debug("[BOARD SERVICE - getBoardCmtByIdx] END");

        return cmt;
    }
}
