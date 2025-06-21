package me.jjh.mapia.webserver.util;

import me.jjh.mapia.webserver.common.code.EmailTplCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : DateUtil
 * author         : JJH
 * date           : 2025-02-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-22        JJH       최초 생성
 */
public class EmailTplUtil {

    /**
     * 이메일 템플릿 치환 ({{변수}} → 값)
     */
    public static String replaceVars(String template, Map<String, String> variables, EmailTplCode emailTplCode) {
        if (StringUtils.isBlank(template) || variables == null || variables.isEmpty()) return template;

        // 데이터 검증
        List<String> requiredKeys = emailTplCode.getRequiredKeys();
        for (String key : requiredKeys) {
            if (!variables.containsKey(key) || variables.get(key) == null || variables.get(key).isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST,
                        "이메일 템플릿 '" + emailTplCode.getMailTplKey() + "'에 필수 값 '" + key + "'이 누락되었습니다.");
            }
        }

        // 템플릿 데이터 치환
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = StringUtils.replace(template, "{{" + entry.getKey() + "}}", entry.getValue());
        }

        return template;
    }

}
