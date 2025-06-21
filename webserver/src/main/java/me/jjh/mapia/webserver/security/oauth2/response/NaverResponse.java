package me.jjh.mapia.webserver.security.oauth2.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class NaverResponse implements OAuth2Response, Serializable {

    @Serial
    private static final long serialVersionUID = 66309452L;

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response"); // naver는 response 안에 담겨서 나온다
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
