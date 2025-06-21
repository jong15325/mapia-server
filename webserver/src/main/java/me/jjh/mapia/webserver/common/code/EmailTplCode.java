package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : EmailTplCode
 * author         : JJH
 * date           : 2025-02-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-28        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum EmailTplCode {

    DEFAULT("DEFAULT", List.of("title", "content", "year"), "기본 템플릿"),
    SIGNUP_VERIFY("SIGNUP_VERIFY", List.of("memberId", "verifyCode", "mailExpTime", "directUrl", "year"), "회원가입 인증코드 발송 템플릿");

    private final String mailTplKey;
    private final List<String> requiredKeys; // 필수 데이터 목록
    private final String desc;

}
