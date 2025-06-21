package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : EmailCode
 * author         : JJH
 * date           : 2025-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-12        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum EmailCode {

    NAVER("naver.com", "Naver"),
    GOOGLE("google.com", "Google"),
    NATE("nate.com", "Nate"),
    DAUM("daum.net", "Daum"),
    HANMAIL("hanmail.net", "Hanmail"),
    YAHOO("yahoo.com", "Yahoo");

    private final String domain;
    private final String name;
}
