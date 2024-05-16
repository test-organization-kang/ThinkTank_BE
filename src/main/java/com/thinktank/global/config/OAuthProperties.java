package com.thinktank.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
@ConfigurationProperties(prefix = "jwt.security.oauth2.client.registration")
public class OAuthProperties {

	@Value("kakao.client-id")
	private String kakaoClientId;

	@Value("kakao.client-secret")
	private String kakaoClientSecret;

}
