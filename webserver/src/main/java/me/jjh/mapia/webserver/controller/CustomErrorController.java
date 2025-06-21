package me.jjh.mapia.webserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.DateFormatCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.response.ErrorResponse;
import me.jjh.mapia.webserver.util.*;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * packageName    : me.jjh.mapia.controller
 * fileName       : ErrorController
 * author         : JJH
 * date           : 2025-01-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-19        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    private final UserSessionUtil userSessionUtil;

    @RequestMapping(value="/info", method = {RequestMethod.GET, RequestMethod.POST})
    public String handleError(HttpServletRequest request, Model model) {

        log.debug("[CUSTOM ERROR CONTROLLER - handleError] START");

        // 상태 코드 및 예외 정보 추출
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;

        // 예외 객체 확인
        Throwable ex = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        boolean isFromException = ex != null;

        // 에러가 발생한 원래 경로
        String originalPath = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        if(!isFromException) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(statusCode)
                    .timestamp(DateUtil.getNow(DateFormatCode.STANDARD))
                    .path(originalPath)
                    .message(EnumUtil.getEnumByFieldValue(ErrorCode.class, "value", statusCode).getMessage())
                    .userId(MaskUtil.maskString(userSessionUtil.getCurrentUserId(), 1, 2))
                    .clientIp(MaskUtil.maskIp(request.getRemoteAddr(), 1))
                    .userAgent(request.getHeader("User-Agent"))
                    .build();

            LogUtil.error(errorResponse, "HTTP");
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("msg1", ErrorMsgUtil.errorMsg(statusCode).get("msg1"));
        model.addAttribute("msg2", ErrorMsgUtil.errorMsg(statusCode).get("msg2"));

        log.debug("[CUSTOM ERROR CONTROLLER - handleError] END");

        return "error/error_info";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/detail", method = RequestMethod.POST)
    public String handleErrorDetails(HttpServletRequest request, Model model) {
        log.debug("[CUSTOM ERROR CONTROLLER - handleErrorDetails] START");

        ServletWebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION));

        model.addAttribute("errors", errorDetails);

        log.debug("[CUSTOM ERROR CONTROLLER - handleErrorDetails] END");

        return "error/error_detail";  // 상세 에러 페이지 연결
    }
}
