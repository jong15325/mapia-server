package me.jjh.mapia.gameserver.util;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : StringUtil
 * author         : JJH
 * date           : 2025-02-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-26        JJH       최초 생성
 */
public class StringUtil {

    /**
     * 문자열이 null 또는 빈 값인지 확인
     */
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }


}
