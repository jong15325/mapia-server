package me.jjh.mapia.webserver.common.exception;

import lombok.Getter;
import me.jjh.mapia.webserver.common.code.Code;
import me.jjh.mapia.webserver.common.code.ErrorCode;

/**
 * packageName    : me.jjh.mapia.common.exception
 * fileName       : BusinessException
 * author         : JJH
 * date           : 2025-01-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-17        JJH       최초 생성
 */

@Getter
public class BusinessException extends BaseException  {

    private final Code code;

    public BusinessException(Code code) {
        super(code);
        this.code = code;
    }

    public BusinessException(Code code, String message) {
        super(code, message);
        this.code = code;
    }
}