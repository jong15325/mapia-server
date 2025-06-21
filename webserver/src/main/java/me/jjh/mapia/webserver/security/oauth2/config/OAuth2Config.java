package me.jjh.mapia.webserver.security.oauth2.config;

import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.properties.Oauth2Properties;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

/**
 *packageName    : me.jjh.mapia.security.oauth2.config
 * fileName       : OAuth2Config
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@RequiredArgsConstructor
@Component
public class OAuth2Config {

    private final Oauth2Properties oauth2Properties;

    /**
     * 네이버 OAuth 요청 클라이언트
     * @return ClientRegistration
     */
    public ClientRegistration naverClientRegistration() {
        Oauth2Properties.Naver naver = oauth2Properties.getNaver();

        return ClientRegistration.withRegistrationId("naver")
                .clientId(naver.getClientId())
                .clientSecret(naver.getClientSecret())
                .redirectUri(naver.getRedirectUri())
                .authorizationUri(naver.getAuthUri())
                .tokenUri(naver.getTokenUri())
                .userInfoUri(naver.getUserInfoUri())
                .scope(naver.getScope().toArray(new String[0]))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("response")
                .build();
    }

    /**
     * 카카오 OAuth 요청 클라이언트
     * @return ClientRegistration
     */
    public ClientRegistration kakaoClientRegistration() {
        Oauth2Properties.Kakao kakao = oauth2Properties.getKakao();

        return ClientRegistration.withRegistrationId("kakao")
                .clientId(kakao.getClientId())
                .clientSecret(kakao.getClientSecret())
                .redirectUri(kakao.getRedirectUri())
                .authorizationUri(kakao.getAuthUri())
                .tokenUri(kakao.getTokenUri())
                .userInfoUri(kakao.getUserInfoUri())
                .scope(kakao.getScope().toArray(new String[0]))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("id")
                .build();
    }
}
