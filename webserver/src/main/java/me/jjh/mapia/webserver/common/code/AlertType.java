package me.jjh.mapia.webserver.common.code;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : AlertType
 * author         : JJH
 * date           : 2025-03-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-16        JJH       최초 생성
 */
public enum AlertType {
    SHOW,       // 단순 메시지 출력
    CONFIRM,    // 확인 버튼 포함
    MOVE,       // 특정 페이지 이동
    CLOSE,      // 창 닫기
    INFO,    // 정보 메시지
    SUCCESS, // 성공 메시지
    WARNING, // 경고 메시지
    ERROR,   // 오류 메시지
    QUESTION, // 질문 메시지
    POST,
    GET,
    AJAX
}
