package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 *packageName    : me.jjh.mapia.common.code
 * fileName       : ErrorCode
 * author         : JJH
 * date           : 2025-01-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-17        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements Code {

    SUCCESS(HttpStatus.OK, 200, "SSS", "OK", ""),

    /* CLIENT */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "EF001", "잘못된 요청입니다", "문제가 지속되면 관리자에게 문의해주세요"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "EF002", "인증이 필요합니다", ""),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "EF003", "접근 권한이 없습니다", ""),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "EF004", "요청한 페이지를 찾을 수 없습니다", "문제가 지속되면 관리자에게 문의해주세요"),

    /* SERVER */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "EF101", "서버 내부 오류가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, 501, "EF102", "요청하신 기능은 아직 지원되지 않습니다", ""),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, 502, "EF103", "서비스 연결 중 오류가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 503, "EF104", "현재 서비스가 일시적으로 중단되었습니다", ""),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, 504, "EF105", "서버 응답 시간이 초과되었습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    REDIS_SAVE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 503, "EF106", "REDIS 저장에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    MAIL_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 503, "EF107", "메일 전송에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    DB_SAVE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 503, "EF108", "DB 저장에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),


    /* BUSINESS */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "EF201", "사용자를 찾을 수 없습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 400, "EF202", "잘못된 요청입니다", "문제가 지속되면 관리자에게 문의해주세요"),
    NOT_RECV_TYPE(HttpStatus.BAD_REQUEST, 400, "EF203", "수신 타입이 지정되지 않았습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    NOT_MAIL_KEY(HttpStatus.BAD_REQUEST, 400, "EF204", "MAIL_TPL_KEY가 없습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    NOT_MAIL_TPL(HttpStatus.BAD_REQUEST, 400, "EF205", "데이터베이스에 템플릿이 없습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    NOT_MAIL_RECV(HttpStatus.BAD_REQUEST, 400, "EF206", "수신자가 없습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, 409, "EF207", "중복된 사용자가 존재합니다", "문제가 지속되면 관리자에게 문의해주세요"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 400, "EF208", "입력이 잘못되었습니다", "입력값을 다시 확인해주세요"),


    ;


    //SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "B003", "서버 오류가 발생했습니다.");

    private final HttpStatus status;  // Spring HttpStatus
    private final int value;          // 고유 넘버
    private final String code;        // 고유 코드
    private final String message;     // 메시지
    private final String addMessage; // 추가 메세지
}
