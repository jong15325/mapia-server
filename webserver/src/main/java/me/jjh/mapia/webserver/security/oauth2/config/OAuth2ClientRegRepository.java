package me.jjh.mapia.webserver.security.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@RequiredArgsConstructor
@Configuration
public class OAuth2ClientRegRepository {

    private final OAuth2Config oauth2Config;

    public org.springframework.security.oauth2.client.registration.ClientRegistrationRepository getClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(oauth2Config.naverClientRegistration(), oauth2Config.kakaoClientRegistration());
    }
}
