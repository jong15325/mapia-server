package me.jjh.mapia.webserver.builder;

/**
 * packageName    : me.jjh.mapia.builder
 * fileName       : LogBuilder
 * author         : JJH
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        JJH       최초 생성
 */
public class LogBuilder {
    private final StringBuilder logBuilder = new StringBuilder();

    /**
     * 필드 추가 메서드
     * @param key 필드 이름
     * @param value 필드 값
     * @return 현재 LogBuilder 객체 (체이닝 가능)
     */
    public LogBuilder addField(String key, Object value) {
        logBuilder.append("\n - ").append(key).append("=").append(value);
        return this;
    }

    /**
     * 조건부 필드 추가 메서드
     * @param condition 조건
     * @param key 필드 이름
     * @param value 필드 값
     * @return 현재 LogBuilder 객체 (체이닝 가능)
     */
    public LogBuilder addFieldIf(boolean condition, String key, Object value) {
        if (condition && value != null) {
            logBuilder.append("\n - ").append(key).append("=").append(value);
        }
        return this;
    }

    /**
     * 로그 메시지 생성
     * @param header 로그 헤더 (예: [EXCEPTION], [ERROR])
     * @param message 메시지 내용
     * @return 완성된 로그 문자열
     */
    public String build(String header, String message) {
        return "\n" + header + " : " + message + logBuilder.toString();
    }

    /**
     * 로그 초기화 (필요 시 동일 인스턴스를 재사용 가능)
     * @return 현재 LogBuilder 객체 (체이닝 가능)
     */
    public LogBuilder reset() {
        logBuilder.setLength(0);
        return this;
    }
}
