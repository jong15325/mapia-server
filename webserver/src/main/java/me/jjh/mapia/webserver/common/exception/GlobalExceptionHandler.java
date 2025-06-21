package me.jjh.mapia.webserver.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.builder.LogBuilder;
import me.jjh.mapia.webserver.common.code.Code;
import me.jjh.mapia.webserver.common.code.DateFormatCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.response.ApiResponse;
import me.jjh.mapia.webserver.common.response.ErrorResponse;
import me.jjh.mapia.webserver.properties.SpringProperties;
import me.jjh.mapia.webserver.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *packageName    : me.jjh.mapia.common.exception
 * fileName       : GlobalExceptionHandler
 * author         : JJH
 * date           : 2025-01-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-17        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final SpringProperties springProperties;

    private final UserSessionUtil userSessionUtil;

    /**
     * 클라이언트 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ClientException.class)
    public Object handleClientException(ClientException ex, HttpServletRequest request) {
        return processException(ex, "CLIENT", request);
    }

    /**
     * 비즈니스 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return processException(ex, "BUSINESS", request);
    }

    /**
     * 서버 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ServerException.class)
    public Object handleServerException(ServerException ex, HttpServletRequest request) {
        return processException(ex, "SERVER", request);
    }

    /**
     * 포괄적 예외 처리
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, HttpServletRequest request) {
        return processException(ex, "GENERAL", request);
    }

    /**
     * 권한 예외 client 예외로 반환
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        throw new ClientException(ErrorCode.FORBIDDEN); // 403을 ClientException으로 변환
    }

    private Object processException(Exception ex, String logCategory, HttpServletRequest request) {
        Code errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        StackTraceElement element = ex.getStackTrace()[0];
        String className = element.getClassName(); // 에러 발생 클래스
        String methodName = element.getMethodName(); // 에러 발생 메서드
        String location = (ex.getStackTrace().length > 0) ? element.toString() : "스택 경로를 추적할 수 없습니다";
        int lineNumber = element.getLineNumber(); // 에러 발생 라인 번호

        // 예외 타입별 처리
        if (ex instanceof ClientException) {
            errorCode = ((ClientException) ex).getCode();
        } else if (ex instanceof BusinessException) {
            errorCode = ((BusinessException) ex).getCode();
        } else if (ex instanceof ServerException) {
            errorCode = ((ServerException) ex).getCode();
        }

        // 공통 에러 응답 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .reasonPhrase(errorCode.getStatus().getReasonPhrase())
                .code(errorCode.getCode())
                .timestamp(DateUtil.getNow(DateFormatCode.STANDARD))
                .path(request.getRequestURI())
                .method(request.getMethod())
                .message(errorCode.getMessage())
                .addMessage(errorCode.getAddMessage())
                .build();

        String activeProfile = springProperties.getProfiles().getActive();
        boolean isLocal = "local".equalsIgnoreCase(activeProfile);

        if (isLocal) {
            errorResponse = errorResponse.toBuilder()
                    .exMessage(ex.getMessage())
                    .className(className)
                    .location(location)
                    .methodName(methodName)
                    .lineNumber(lineNumber)
                    .userId(MaskUtil.maskString(userSessionUtil.getCurrentUserId(), 1, 2))
                    .clientIp(MaskUtil.maskIp(UserAgentUtil.getClientIp(request), 1))
                    .userAgent(UserAgentUtil.getUserAgentInfo(request)).build();
        }

        // 로그 기록
        LogUtil.exception(errorResponse, logCategory);

        log.debug("Exception message: '{}'", ex.getMessage());

        // API 요청인지 확인 후 응답 처리
        if (isApiRequest(request)) {
            return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.error(errorCode));
        }

        // JSP 뷰로 전달하기 위해 에러 정보를 설정
        request.setAttribute("jakarta.servlet.error.status_code", errorCode.getStatus().value());
        request.setAttribute("jakarta.servlet.error.exception", ex);

        return "forward:/error/info"; // CustomErrorController로 포워드
    }

    /**
     * 일반 요청과 AJAX 요청 분기
     * @param request
     * @return
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");

        return (accept != null && accept.contains("application/json")) ||
                ("XMLHttpRequest".equalsIgnoreCase(requestedWith));
    }
}
