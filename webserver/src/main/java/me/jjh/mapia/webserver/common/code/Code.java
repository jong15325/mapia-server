package me.jjh.mapia.webserver.common.code;

import org.springframework.http.HttpStatus;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : Code
 * author         : JJH
 * date           : 2025-01-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-18        JJH       최초 생성
 */
public interface Code {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
    String getAddMessage();
}
