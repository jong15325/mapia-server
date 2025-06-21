package me.jjh.mapia.webserver.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : EnumUtil
 * author         : JJH
 * date           : 2025-01-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-19        JJH       최초 생성
 */

public class EnumUtil {

    /**
     * 특정 enum 클래스에서 변수 이름과 값으로 enum 객체를 반환.
     *
     * @param enumClass   enum 클래스
     * @param fieldName   변수 이름
     * @param fieldValue  변수 값
     * @param <E>         enum 타입의 제네릭
     * @return 해당하는 enum 객체 또는 null
     */
    public static <E extends Enum<E>> E getEnumByFieldValue(Class<E> enumClass, String fieldName, Object fieldValue) {
        if (enumClass == null || fieldName == null || fieldValue == null) {
            throw new IllegalArgumentException("Enum 클래스, 변수 이름 및 변수 값은 null일 수 없습니다.");
        }

        for (E enumConstant : enumClass.getEnumConstants()) {
            try {
                Field field = enumClass.getDeclaredField(fieldName);
                field.setAccessible(true); // private 필드 접근 허용
                Object value = field.get(enumConstant);

                if (fieldValue.equals(value)) {
                    return enumConstant;
                }
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(
                        "필드 '" + fieldName + "'은(는) " + enumClass.getSimpleName() + " 클래스에 존재하지 않습니다.", e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(
                        "필드 '" + fieldName + "'에 접근할 수 없습니다.", e);
            }
        }
        return null; // 일치하는 enum 객체가 없을 경우
    }

    /**
     * 특정 Enum 클래스의 전체 값을 리스트로 반환
     *
     * @param enumClass 조회할 Enum 클래스
     * @param <E>       Enum 타입의 제네릭
     * @return 해당 Enum의 전체 리스트
     */
    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum 클래스는 null일 수 없습니다.");
        }

        List<E> enumList = new ArrayList<>();
        for (E enumConstant : enumClass.getEnumConstants()) {
            enumList.add(enumConstant);
        }
        return enumList;
    }
}
