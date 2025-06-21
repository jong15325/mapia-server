package me.jjh.mapia.webserver.common.exception;

import lombok.Getter;
import me.jjh.mapia.webserver.common.code.Code;
import me.jjh.mapia.webserver.common.code.ErrorCode;

/**
 * packageName    : me.jjh.mapia.common.exception
 * fileName       : ServerException
 * author         : JJH
 * date           : 2025-01-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-18        JJH       최초 생성
 */

@Getter
public class ServerException extends BaseException {

    private final Code code;

    public ServerException(Code code) {
        super(code);
        this.code = code;
    }

    public ServerException(Code code, String message) {
        super(code, message);
        this.code = code;
    }
}
