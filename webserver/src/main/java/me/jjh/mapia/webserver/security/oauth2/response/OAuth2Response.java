package me.jjh.mapia.webserver.security.oauth2.response;

public interface OAuth2Response {

    //제공자
    String getProvider();

    //제공자에서 발급해주는 아이디
    String getProviderId();

    //이메일
    String getEmail();

    //사용자 이름
    String getName();
}
