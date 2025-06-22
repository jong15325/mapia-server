package me.jjh.mapia.gameserver.util;

import me.jjh.mapia.gameserver.code.DateFormatCode;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : DateUtil
 * author         : JJH
 * date           : 2025-03-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-01        JJH       최초 생성
 */
public class DateUtil {

    /**
     * 현재 시간을 포맷 적용해서 출력
     */
    public static String getNow(DateFormatCode dateFormatCode) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormatCode.getPattern()));
    }

    /**
     * 현재 시간에서 특정 시간 단위를 추가 또는 차감
     */
    public static LocalDateTime adjustDateTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    /**
     * 주어진 만료 시간이 현재 시간보다 이전인지 확인 (만료 여부 체크)
     */
    public static boolean isExpired(Object expAt) {
        if (expAt == null) {
            return false; // 만료 시간이 없으면 기본적으로 유효한 것으로 간주
        }

        // 현재 시간 기준 (LocalDateTime)
        LocalDateTime now = LocalDateTime.now();

        // expAt 타입 변환
        LocalDateTime expirationTime;

        if (expAt instanceof LocalDateTime) {
            expirationTime = (LocalDateTime) expAt;
        } else {
            expirationTime = convertDate(expAt, LocalDateTime.class, false);
        }

        return expirationTime.isBefore(now);
    }

    /**
     * 날짜/시간 타입 변환 메서드 (LocalDateTime, Date, Instant 간 변환)
     */
    public static <T> T convertDate(Object inputDateTime, Class<T> targetType, boolean isNull) {
        if (inputDateTime == null) {
            if (isNull)
                return null;
            else
                throw new IllegalArgumentException("날짜 변환 실패 : " + inputDateTime.getClass().getName() + " -> " + targetType.getName());
        }

        if (targetType.isInstance(inputDateTime)) {
            return targetType.cast(inputDateTime);
        }

        ZoneId zoneId = ZoneId.systemDefault();

        // LocalDateTime -> 변환
        if (inputDateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) inputDateTime;
            if (targetType.equals(Date.class)) return targetType.cast(Date.from(localDateTime.atZone(zoneId).toInstant()));
            if (targetType.equals(Long.class)) return targetType.cast(localDateTime.atZone(zoneId).toInstant().toEpochMilli());
        }

        // Date -> 변환
        if (inputDateTime instanceof Date) {
            Date date = (Date) inputDateTime;
            if (targetType.equals(LocalDateTime.class)) return targetType.cast(date.toInstant().atZone(zoneId).toLocalDateTime());
            if (targetType.equals(Long.class)) return targetType.cast(date.getTime());
        }

        // Long (밀리초) -> 변환
        if (inputDateTime instanceof Long) {
            long millis = (Long) inputDateTime;
            if (targetType.equals(LocalDateTime.class)) return targetType.cast(Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDateTime());
            if (targetType.equals(Date.class)) return targetType.cast(new Date(millis));
        }

        throw new IllegalArgumentException("날짜 변환 실패 : " + inputDateTime.getClass().getName() + " -> " + targetType.getName());
    }

    /**
     * 날짜 타입을 문자열로 변환 (LocalDateTime, Date, Long, String 지원)
     */
    public static String formatDateTime(Object dateTime, DateFormatCode dateFormatCode) {
        if (dateTime == null || dateFormatCode == null) {
            return null;
        }

        LocalDateTime localDateTime;

        // 다양한 타입을 LocalDateTime으로 변환
        if (dateTime instanceof LocalDateTime) {
            localDateTime = (LocalDateTime) dateTime;
        } else if (dateTime instanceof Date) {
            localDateTime = ((Date) dateTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (dateTime instanceof Long) {
            localDateTime = Instant.ofEpochMilli((Long) dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else {
            throw new IllegalArgumentException("날짜 포맷 변환 실패 : " + dateTime.getClass().getName());
        }

        // 변환된 LocalDateTime을 지정된 포맷으로 변환하여 반환
        return localDateTime.format(DateTimeFormatter.ofPattern(dateFormatCode.getPattern()));
    }

}
