package me.jjh.mapia.webserver.util;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : ErrorMsgUtil
 * author         : JJH
 * date           : 2025-01-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-19        JJH       최초 생성
 */

public class ErrorMsgUtil {

    public static Map<String, Object> errorMsg(int statusCode) {

        Map<String, Object> map = new HashMap<String, Object>();

        String msg1 = "";
        String msg2 = "";

        // 에러 코드에 따라 메시지 설정
        switch (statusCode) {
            case 400:
                msg1 = "죄송해요, 잘못된 요청이에요";
                msg2 = "동일한 문제가 계속 발생하면 고객센터로 문의해주세요!";
                break;
            case 401:
                msg1 = "죄송해요, 로그인이 필요한 작업이에요";
                msg2 = "로그인 후에도 문제가 발생하면 고객센터로 문의해주세요!";
                break;
            case 403:
                msg1 = "죄송해요, 접근 권한이 없어요";
                msg2 = "접근 권한이 있다면 고객센터로 문의해주세요!";
                break;
            case 404:
                msg1 = "죄송해요, 요청한 페이지를 찾지 못했어요";
                msg2 = "동일한 문제가 계속 발생하면 고객센터로 문의해주세요!";
                break;
            case 500:
                msg1 = "죄송해요, 요청한 작업을 수행할 수 없어요";
                msg2 = "동일한 문제가 계속 발생하면 고객센터로 문의해주세요!";
                break;
            case 502:
            case 503:
            case 504:
                msg1 = "죄송해요, 현재 서비스 이용이 불가능해요";
                msg2 = "동일한 문제가 계속 발생하면 고객센터로 문의해주세요!";
                break;
            default:
                msg1 = "죄송해요,  예기치 않은 오류가 발생했어요";
                msg2 = "동일한 문제가 계속 발생하면 고객센터로 문의해주세요!";
        }

        map.put("msg1", msg1);
        map.put("msg2", msg2);

        return map;
    }
}
