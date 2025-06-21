package me.jjh.mapia.webserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.exception.ServerException;

import java.util.Map;

@Slf4j
public class MapUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 중첩된 Map에서 경로를 기반으로 특정 값을 가져옵니다.
     * 예: "key1.key2.key3" 형태의 경로를 통해 값에 접근
     *
     * @param map  원본 Map
     * @param path 가져오려는 경로 (예: "key1.key2.key3")
     * @return 경로에 해당하는 값 또는 null
     */
    public static Object getValue(Map<String, Object> map, String path) {
        if (map == null || path == null || path.isEmpty()) {
            return null;
        }

        String[] keys = path.split("\\.");
        Object current = map;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            } else {
                return null; // 경로가 유효하지 않을 경우 null 반환
            }
        }

        return current; // 최종 값 반환
    }

    /**
     * 중첩된 Map에서 경로를 기반으로 특정 String 값을 가져옵니다.
     *
     * @param map  원본 Map
     * @param path 가져오려는 경로 (예: "key1.key2.key3")
     * @return 경로에 해당하는 String 값 또는 null
     */
    public static String getStringValue(Map<String, Object> map, String path) {
        Object value = getValue(map, path);
        return value instanceof String ? (String) value : null;
    }

    /**
     * 중첩된 Map에서 경로를 기반으로 특정 Map 값을 가져옵니다.
     *
     * @param map  원본 Map
     * @param path 가져오려는 경로 (예: "key1.key2.key3")
     * @return 경로에 해당하는 Map 값 또는 null
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNestedMap(Map<String, Object> map, String path) {
        Object value = getValue(map, path);
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    /**
     * Map을 JSON 문자열로 변환합니다.
     *
     * @param map 변환할 Map 객체
     * @return JSON 문자열
     */
    public static String toJson(Map<String, Object> map) {
        if (DataVaildUtil.isMapEmpty(map)) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.warn("[MapUtil - toJson] json 변환에 실패했습니다 : [{}]",e.getMessage());
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * JSON 문자열을 Map으로 변환합니다.
     *
     * @param json 변환할 JSON 문자열
     * @return 변환된 Map 객체
     */
    public static Map<String, Object> fromJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.warn("[MapUtil - toJson] Map 변환에 실패했습니다 : [{}]",e.getMessage());
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
