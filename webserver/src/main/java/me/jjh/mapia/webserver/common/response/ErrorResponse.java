package me.jjh.mapia.webserver.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import me.jjh.mapia.webserver.common.code.DateFormatCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.util.DateUtil;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * packageName    : me.jjh.mapia.common.response
 * fileName       : ViewErrorResponse
 * author         : JJH
 * date           : 2025-01-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-17        JJH       최초 생성
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true) // 사용할 변수가 많을 때 유용
@Getter
public class ErrorResponse {
    /* 기본 로그 */
    private final int status;         // HTTP 상태 코드
    private final String reasonPhrase; // HTTP 에러 이유
    private final String code;        // 에러 코드
    private final String timestamp;   // 에러 발생 시간
    private final String path;        // 요청 URI
    private final String method;      // 요청 메서드 (GET, POST 등)
    private final String message;     // 사용자 메시지
    private final String addMessage;  // 추가 메세지
    private final String exMessage;

    /* 디버그 로그 */
    private final String className; // 예외 클래스 이름
    private final String location; // 스택 트레이스 요소
    private final String methodName; // 예외 메소드 이름
    private final int lineNumber; // 예외 코드 라인

    /* 보안 로그 */
    private final String userId;      // 사용자 ID (옵션)
    private final String clientIp;    // 요청자의 IP 주소
    private final String userAgent;   // 사용자 브라우저 정보
    // private final String traceId;     // 분산 시스템에서의 Trace ID

}
