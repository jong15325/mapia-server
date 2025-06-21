package me.jjh.mapia.webserver.security.oauth2.response;

import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.util.MapUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response , Serializable {

    @Serial
    private static final long serialVersionUID = 6093845L;

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        //log.debug("반환 이메일 : {}", MapUtil.getStringValue(attribute, "kakao_account.email"));
        return MapUtil.getStringValue(attribute, "kakao_account.email");
    }

    @Override
    public String getName() {
        return MapUtil.getStringValue(attribute, "kakao_account.profile.nickname");
    }
}

/*
* {
  "id": "1042695122",
  "connected_at": "2019-03-12T16:06:52Z",
  "properties": {
    "nickname": "정종한",
    "profile_image": "http://k.kakaocdn.net/dn/soCYw/btsGoWw96ma/0Eu6aVk4RuWRC5rcBhVsH0/img_640x640.jpg",
    "thumbnail_image": "http://k.kakaocdn.net/dn/soCYw/btsGoWw96ma/0Eu6aVk4RuWRC5rcBhVsH0/img_110x110.jpg"
  },
  "kakao_account": {
    "profile_needs_agreement": false,
    "profile": {
      "nickname": "정종한",
      "thumbnail_image_url": "http://k.kakaocdn.net/dn/soCYw/btsGoWw96ma/0Eu6aVk4RuWRC5rcBhVsH0/img_110x110.jpg",
      "profile_image_url": "http://k.kakaocdn.net/dn/soCYw/btsGoWw96ma/0Eu6aVk4RuWRC5rcBhVsH0/img_640x640.jpg",
      "is_default_image": false,
      "is_default_nickname": false
    },
    "has_email": true,
    "email_needs_agreement": false,
    "is_email_valid": true,
    "is_email_verified": true,
    "email": "howeer15325@naver.com"
  }
}
*
* */