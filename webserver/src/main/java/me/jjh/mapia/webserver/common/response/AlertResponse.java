package me.jjh.mapia.webserver.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.Code;
import me.jjh.mapia.webserver.common.code.ComCode;

/**
 * packageName    : me.jjh.mapia.common.response
 * fileName       : AlertResponse
 * author         : JJH
 * date           : 2025-01-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-18        JJH       최초 생성
 */

@RequiredArgsConstructor
@Data
public class AlertResponse {
    private final Code enumCode; // ComCode Eum
    private final AlertType category; // 알럿 타입
    private final AlertType successMethod;
    private final String successUrl; // 이동할 Url
    private final String successData;
    private final AlertType cancelMethod;
    private final String cancelUrl; // 이동할 Url2
    private final String cancelData;

    // 기본 생성자 (최소 필수 값: enumCode, alertType)
    public AlertResponse(Code enumCode, AlertType category) {
        this(enumCode, category, null, null, null, null, null, null);
    }

    // GET 단순 응답  (enumCode, alertType, url)
    public AlertResponse(Code enumCode, AlertType category, String successUrl) {
        this(enumCode, category, AlertType.GET, successUrl, null, null, null, null);
    }

    // 데이터 포함 응답
    public AlertResponse(Code enumCode, AlertType category, AlertType successMethod, String successUrl, String successData) {
        this(enumCode, category, successMethod, successUrl, successData, null, null, null);
    }

    // 데이터 미포함 응답
    public AlertResponse(Code enumCode, AlertType category, AlertType successMethod, String successUrl, AlertType cancelMethod, String cancelUrl) {
        this(enumCode, category, successMethod, successUrl, null, cancelMethod, cancelUrl, null);
    }

    // 데이터 포함 응답 (성공만)
    public AlertResponse(Code enumCode, AlertType category, AlertType successMethod, String successUrl, String successData, AlertType cancelMethod, String cancelUrl) {
        this(enumCode, category, successMethod, successUrl, successData, cancelMethod, cancelUrl, null);
    }
}
