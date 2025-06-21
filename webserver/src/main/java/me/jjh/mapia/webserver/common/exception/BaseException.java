package me.jjh.mapia.webserver.common.exception;

import lombok.Getter;
import me.jjh.mapia.webserver.common.code.Code;

/**
 *packageName    : me.jjh.mapia.common.exception
 * fileName       : BaseException
 * author         : JJH
 * date           : 2025-01-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-18        JJH       최초 생성
 */

//직접 인스턴스화 방지 - 추상화
@Getter
public class BaseException extends RuntimeException {

    private final Code code;

    // 코드 메세지 입력
    protected BaseException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    // 직접 메세지 입력
    protected BaseException(Code code, String message) {
        super(message);
        this.code = code;
    }
}
