package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *packageName    : me.jjh.mapia.common.code
 * fileName       : DateFormatCode
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
public enum DateFormatCode {

    STANDARD("yyyy-MM-dd HH:mm:ss"),

    STANDARD_KR("yyyy년 MM월 dd일 HH시 mm분 ss초"),

    DATE_ONLY("yyyy-MM-dd"),

    DATE_ONLY_KR("yyyy년 MM월 dd일"),

    TIME_ONLY("HH:mm:ss"),

    TIME_ONLY_KR("HH:mm:ss"),

    DATE_TIME("yyyy-MM-dd HH:mm"),

    DATE_TIME_KR("yyyy년 MM월 dd일 HH시 mm분");

    private final String pattern;
}
