package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : StatusCode
 * author         : JJH
 * date           : 2025-03-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-03        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // 성공 상태
    SUCCESS(200, "SUCCESS", "성공"),
    CREATED(201, "CREATED", "생성됨"),
    ACCEPTED(202, "ACCEPTED", "요청 접수됨"),

    // 일반적인 처리 상태
    PENDING(1001, "PENDING", "처리 중"),
    APPROVED(1002, "APPROVED", "승인됨"),
    REJECTED(1003, "REJECTED", "거절됨"),
    CANCELED(1004, "CANCELED", "취소됨"),
    WAIT(1005, "WAIT", "대기 중"),
    
    // 실패 상태
    FAILED(1005, "FAILURE", "실패"),
    VALIDATION_FAILED(1006, "VALIDATION_FAILED", "입력값 검증 실패"),
    PROCESS_FAILED(1007, "PROCESS_FAILED", "처리 실패"),
    TIMEOUT(1008, "TIMEOUT", "시간 초과"),
    DUPLICATE_REQUEST(1009, "DUPLICATE_REQUEST", "중복 요청"),
    EXPIRED(1010, "EXPIRED", "요청 또는 세션 만료"),
    LIMIT_EXCEEDED(1011, "LIMIT_EXCEEDED", "허용된 한도 초과"),
    NOT_ALLOWED(1012, "NOT_ALLOWED", "허용되지 않음"),
    IN_PROGRESS(1013, "IN_PROGRESS", "진행 중");

    private final int code;
    private final String value;
    private final String message;
}
