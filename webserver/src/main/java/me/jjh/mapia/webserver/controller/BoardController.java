package me.jjh.mapia.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.code.EmailCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import me.jjh.mapia.webserver.common.response.ApiResponse;
import me.jjh.mapia.webserver.dto.board.BoardCmtDTO;
import me.jjh.mapia.webserver.dto.board.BoardSearchDTO;
import me.jjh.mapia.webserver.dto.board.BoardDTO;
import me.jjh.mapia.webserver.properties.FileProperties;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.service.board.BoardService;
import me.jjh.mapia.webserver.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * packageName    : me.jjh.mapia.controller
 * fileName       : BoardController
 * author         : JJH
 * date           : 2025-01-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-11        JJH       최초 생성
 */


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserSessionUtil userSessionUtil;
    private final FileProperties fileProperties;

    /*@PreAuthorize("hasRole('ADMIN')")*/
    @GetMapping("list")
    public String list(@ModelAttribute BoardSearchDTO boardSearchDTO, Model model) {

        log.debug("[BOARD CONTROLLER - list] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ReturnUtil.move(model, new AlertResponse(ComCode.LOGIN_SESSION_EXPIRED, AlertType.WARNING, "/"));
        }

        PageInfo<BoardDTO> boardList = boardService.getBoardList(boardSearchDTO);

        // 게시판 데이터
        model.addAttribute("boardList", boardList.getList());
        model.addAttribute("pageInfo", boardList);
        model.addAttribute("search", boardSearchDTO);

        log.debug("[BOARD CONTROLLER - list] END");

        return "board/board_list";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("create")
    public String create(@ModelAttribute BoardSearchDTO boardSearchDTO, Model model) {

        log.debug("[BOARD CONTROLLER - create] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ReturnUtil.move(model, new AlertResponse(ComCode.LOGIN_SESSION_EXPIRED, AlertType.WARNING, "/"));
        }

        if(DataVaildUtil.isStringEmpty(boardSearchDTO.getCtgType())) {
            return ReturnUtil.move(model, new AlertResponse(ErrorCode.BAD_REQUEST, AlertType.WARNING, "/"));
        }

        model.addAttribute("search", boardSearchDTO);
        model.addAttribute("boardTagList", boardService.getBoardTagList(boardSearchDTO));

        // FileProperties 객체를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        // FileProperties 객체를 Map으로 변환
        Map<String, Object> fileConfigMap = objectMapper.convertValue(fileProperties, Map.class);

        // Map을 JSON 문자열로 변환
        String fileConfigJson = MapUtil.toJson(fileConfigMap);

        // JSON 문자열 이스케이프 없이 바로 모델에 추가
        model.addAttribute("fileConfig", fileConfigJson);
        model.addAttribute("search", boardSearchDTO);

        log.debug("[BOARD CONTROLLER - create] END");

        return "board/board_create";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createGet() {
        log.debug("[BOARD CONTROLLER - createGet] START");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Success");

        log.debug("[BOARD CONTROLLER - createGet] END");

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("createProc")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> createProc(@ModelAttribute BoardDTO boardDTO) {

        log.debug("[BOARD CONTROLLER - createProc] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(ComCode.LOGIN_SESSION_EXPIRED, "/auth/login"));
        }

        Long memberIdx = userOpt.get().getMemberIdx();
        String memberId = userOpt.get().getMemberId();

        // 게시글 등록 처리
        boardDTO.setCreatedIdx(memberIdx);
        boardDTO.setCreatedId(memberId);
        Long boardIdx = boardService.createBoard(boardDTO);

        HashMap<String, Object> data = new HashMap<>();
        data.put("boardIdx", boardIdx);
        data.put("ctgType", boardDTO.getBoardCtg().getBoardCtgType());

        log.debug("[BOARD CONTROLLER - createProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.INSERT_SUCCESS, data));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("read")
    public String read(@ModelAttribute BoardSearchDTO boardSearchDTO, Model model) {

        log.debug("[BOARD CONTROLLER - read] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ReturnUtil.move(model, new AlertResponse(ComCode.LOGIN_SESSION_EXPIRED, AlertType.WARNING, "/"));
        }

        //조회수 udpate
        boardService.updateBoardViewCount(boardSearchDTO.getSearchIdx());

        Map<String, Object> returnMap= boardService.getBoardByIdx(boardSearchDTO.getSearchIdx());
        model.addAttribute("boardDTO", returnMap.get("boardDTO"));
        model.addAttribute("fileList", returnMap.get("fileList"));
        model.addAttribute("cmtList", returnMap.get("cmtList"));

        log.debug("[BOARD CONTROLLER - read] END");

        return "board/board_read";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/cmt/createProc")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> cmtCreateProc(@ModelAttribute BoardCmtDTO boardCmtDTO) {

        log.debug("[BOARD CONTROLLER - cmtCreateProc] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error(ComCode.LOGIN_SESSION_EXPIRED));
        }

        Long memberIdx = userOpt.get().getMemberIdx();
        String memberId = userOpt.get().getMemberId();

        boardCmtDTO.setCreatedIdx(memberIdx);
        boardCmtDTO.setCreatedId(memberId);

        // 댓글 저장 및 저장된 댓글 정보 반환
        BoardCmtDTO savedComment = boardService.createCmtBoard(boardCmtDTO);

        log.debug("[BOARD CONTROLLER - cmtCreateProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.INSERT_SUCCESS, savedComment));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/reply/createProc")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> replyCreateProc(@ModelAttribute BoardCmtDTO boardCmtDTO) {

        log.debug("[BOARD CONTROLLER - replyCreateProc] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error(ComCode.LOGIN_SESSION_EXPIRED));
        }

        Long memberIdx = userOpt.get().getMemberIdx();
        String memberId = userOpt.get().getMemberId();

        boardCmtDTO.setCreatedIdx(memberIdx);
        boardCmtDTO.setCreatedId(memberId);

        // 댓글 저장 및 저장된 댓글 정보 반환
        BoardCmtDTO savedReply = boardService.createCmtBoard(boardCmtDTO);

        log.debug("[BOARD CONTROLLER - replyCreateProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.INSERT_SUCCESS, savedReply));
    }
}
