package me.jjh.mapia.gameserver.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : DataVaildUtil
 * author         : JJH
 * date           : 2025-02-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-27        JJH       최초 생성
 */

public class DataVaildUtil {

    public static boolean isStringEmpty(String str) {
        return StringUtils.isBlank(str); // null, "", " " 모두 true 반환
    }

    public static boolean isListEmpty(List<?> list) {
        return CollectionUtils.isEmpty(list); // null 또는 빈 리스트이면 true
    }

    public static boolean isMapEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isSetEmpty(Set<?> set) {
        return set == null || set.isEmpty();
    }

    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNumberNull(Number num) {
        return num == null;
    }

    public static boolean isEnumNull(Enum<?> enumValue) {
        return enumValue == null;
    }

    public static boolean isOptionalEmpty(Optional<?> optional) {
        return optional == null || optional.isEmpty();
    }

    public static boolean isLongEmpty(Long value) {
        return value == null || value <= 0;
    }

    public static boolean isObjectEmpty(Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    public static String escapeHtml(String text) {
        if (isStringEmpty(text)) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#039;");
    }

    public static String newlineToBr(String text) {
        if (isStringEmpty(text)) {
            return "";
        }

        return text.replace("\n", "<br>");
    }

    public static String getSafeHtml(String text) {
        if (isStringEmpty(text)) {
            return "";
        }

        return newlineToBr(escapeHtml(text));
    }

}
