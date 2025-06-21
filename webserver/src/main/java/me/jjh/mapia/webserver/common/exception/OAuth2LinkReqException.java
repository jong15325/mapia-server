package me.jjh.mapia.webserver.common.exception;

import me.jjh.mapia.webserver.security.oauth2.response.OAuth2Response;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * packageName    : me.jjh.mapia.common.exception
 * fileName       : CustomOAuth2Exception
 * author         : JJH
 * date           : 2025-03-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-20        JJH       최초 생성
 */
public class OAuth2LinkReqException extends OAuth2AuthenticationException {
    private final OAuth2Response oAuth2Response;

    public OAuth2LinkReqException(String message, OAuth2Response oAuth2Response) {
        super(message);
        this.oAuth2Response = oAuth2Response;
    }

    public OAuth2Response getOAuth2Response() {
        return oAuth2Response;
    }
}
