package me.jjh.mapia.gameserver.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : VerificationCode
 * author         : JJH
 * date           : 2025-03-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-02        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum RedisKeyCode {

    SIGNUP_EMAIL("SIGNUP", "EMAIL","회원가입 인증", Duration.ofHours(1)),
    FIND_PASSWORD_EMAIL("FIND_PASSWORD", "EMAIL", "비밀번호 찾기", Duration.ofHours(1)),
    USER_SESSION("USER_SESSION", "LOGIN", "사용자 세션", Duration.ofHours(7)),
    USER_SESSION_MAP("USER_SESSION_MAP", "LOGIN", "사용자 세션맵", Duration.ofHours(7)),

    ;
    private final String type;
    private final String method;
    private final String desc;
    private final Duration duration;
}
