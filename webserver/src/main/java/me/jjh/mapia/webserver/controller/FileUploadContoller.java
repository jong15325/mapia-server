package me.jjh.mapia.webserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import me.jjh.mapia.webserver.common.response.ApiResponse;
import me.jjh.mapia.webserver.dto.board.BoardDTO;
import me.jjh.mapia.webserver.dto.board.BoardFileDTO;
import me.jjh.mapia.webserver.properties.FileProperties;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.service.file.FileService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import me.jjh.mapia.webserver.util.ReturnUtil;
import me.jjh.mapia.webserver.util.SecurityUtil;
import me.jjh.mapia.webserver.util.UserSessionUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * packageName    : me.jjh.mapia.controller
 * fileName       : FileUploadContoller
 * author         : JJH
 * date           : 2025-04-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-09        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/file")
public class FileUploadContoller {

    private final FileProperties fileProperties;
    private final FileService fileService;
    private final UserSessionUtil userSessionUtil;

    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> upload(@RequestParam("file") MultipartFile file,
                                                      @RequestParam Map<String, Object> params) {
        log.debug("[BOARD CONTROLLER - fileUpload] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(ComCode.LOGIN_SESSION_EXPIRED, "/auth/login"));
        }

        Map<String, Object> data = new HashMap<>();
        log.debug("Received file: {} (size: {} bytes, content type: {})",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType());

        int order = 0;
        Long boardIdx = null;
        String fileType = "none";

        try {

            if (params.containsKey("order")) {
                order = Integer.parseInt(params.get("order").toString());
            }

            if (params.containsKey("boardIdx")) {
                boardIdx = Long.parseLong(params.get("boardIdx").toString());
            } else {
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_FAIL));
            }

            if (params.containsKey("fileType")) {
                fileType = params.get("fileType").toString();
            }

            if (file.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_EMPTY));
            }

            if (file.getSize() > fileProperties.getMaxFileSize()) {
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_MAX_SIZE));
            }

            if (file.getSize() < fileProperties.getMinFileSize()) {
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_MIN_SIZE));
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

                if (!fileProperties.isAllowedExtension(extension)) {
                    return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_EXT_ALLOWED));
                }
            } else {
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_NOT_EXT));
            }

            String datePath = new SimpleDateFormat(fileProperties.getDirectoryFormat()).format(new Date());
            String uploadDir = fileProperties.getPath() + "/" + fileType + "/" + datePath;

            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                log.debug("Created directory: {}", uploadDir);
            }

            String savedFilename = UUID.randomUUID().toString() + "." + extension;

            Path filePath = Paths.get(uploadDir, savedFilename);
            Files.copy(file.getInputStream(), filePath);

            log.debug("File saved: {}", filePath);

            BoardFileDTO fileDTO = new BoardFileDTO();
            fileDTO.setBoardIdx(boardIdx);
            fileDTO.setOrgFileName(originalFilename);
            fileDTO.setSavedFileName(savedFilename);
            fileDTO.setFileType(fileType);
            fileDTO.setFilePath(datePath);
            fileDTO.setFileExtension(extension);
            fileDTO.setFileSize(file.getSize());
            fileDTO.setFileContentType(file.getContentType());
            fileDTO.setFileOrder(order);
            fileDTO.setFileKey(SecurityUtil.generateFileKey(boardIdx, fileType, file.getSize(), userOpt.get().getMemberIdx()));
            fileDTO.setIsDeleted("N");
            fileDTO.setCreatedIdx(userOpt.get().getMemberIdx());
            fileDTO.setCreatedId(userOpt.get().getMemberId());
            fileService.insertBoardFile(fileDTO);

            data.put("success", true);
            data.put("fileInfo", fileDTO);
            data.put("message", "파일 업로드 성공");

        } catch (Exception e) {
            log.error("[FILE CONTROLLER - upload] 파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_UPLOAD_FAIL));
        }

        log.debug("[BOARD CONTROLLER - fileUpload] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.FILE_UPLOAD_SUCCES, data));
    }

    /**
     * 파일 다운로드
     * @param fileKey
     * @return 파일 다운로드 응답
     */
    // 파일 다운로드는 스트림 형식으로 반환해야하며 다운로드 실패시 apiResonse 사용을 위해 ? 객체 사용
    @GetMapping("/download/{fileKey}")
    public ResponseEntity<?> download(@PathVariable("fileKey") String fileKey) {
        log.debug("[FILE CONTROLLER - download] START - 다운로드 키 [{}]", fileKey);

        try {
            // 다운로드 키로 파일 ID 조회
            BoardFileDTO boardFileDTO = fileService.getBoardFileByKey(fileKey);
            if (DataVaildUtil.isObjectEmpty(boardFileDTO)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ComCode.FILE_DOWNLOAD_NOT_FOUND));
            }

            Path filePath = Paths.get(fileProperties.getPath() + "/" + boardFileDTO.getFileType() + "/" + boardFileDTO.getFilePath()).resolve(boardFileDTO.getSavedFileName());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                log.error("[FILE CONTROLLER - download] 해당 경로에 파일이 존재하지 않습니다 path[{}]", filePath);
                return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_DOWNLOAD_NOT_FOUND));
            }

            // 다운로드 파일명 인코딩
            String encodedFileName = URLEncoder.encode(boardFileDTO.getOrgFileName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

            // 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);

            // Content-Type 설정
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (StringUtils.hasText(boardFileDTO.getFileContentType())) {
                try {
                    mediaType = MediaType.parseMediaType(boardFileDTO.getFileContentType());
                } catch (Exception e) {
                    log.warn("[FILE CONTROLLER - download] 허용되지 않는 콘텐트 타입입니다 [{}]", boardFileDTO.getFileContentType());
                }
            }

            log.debug("[FILE CONTROLLER - download] END");

            return ResponseEntity.ok().headers(headers).contentType(mediaType).body(resource);

        } catch (MalformedURLException e) {
            log.error("[FILE CONTROLLER - download] MalformedURLException [{}]", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_DOWNLOAD_BAD_REQUEST));
        } catch (Exception e) {
            log.error("[FILE CONTROLLER - download] Exception [{}]", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ComCode.FILE_DOWNLOAD_BAD_REQUEST));
        }
    }

}
