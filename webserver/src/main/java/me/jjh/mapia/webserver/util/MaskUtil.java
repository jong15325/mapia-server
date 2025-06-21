package me.jjh.mapia.webserver.util;

public class MaskUtil {

    /**
     * 일반 문자열 마스킹 (지정한 시작 및 끝 길이를 제외하고 마스킹)
     * @param value 마스킹할 문자열
     * @param startLength 시작 부분 유지 길이
     * @param endLength 끝 부분 유지 길이
     * @return 마스킹된 문자열
     */
    public static String maskString(String value, int startLength, int endLength) {
        if (value == null || value.length() <= startLength + endLength) {
            return "*".repeat(value == null ? 0 : value.length()); // 전체 마스킹
        }
        String maskedPart = "*".repeat(value.length() - startLength - endLength);
        return value.substring(0, startLength) + maskedPart + value.substring(value.length() - endLength);
    }

    /**
     * ip 마스킹 (지정한 구간 수만 노출)
     * @param ip
     * @param visibleSegments
     * @return
     */
    public static String maskIp(String ip, int visibleSegments) {
        if (ip == null || ip.isEmpty()) return "***.***.***.***";
        String[] parts = ip.split("\\.");
        StringBuilder maskedIp = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i < visibleSegments || i >= parts.length - visibleSegments) {
                maskedIp.append(parts[i]);
            } else {
                maskedIp.append("***");
            }
            if (i < parts.length - 1) maskedIp.append(".");
        }
        return maskedIp.toString();
    }
}
