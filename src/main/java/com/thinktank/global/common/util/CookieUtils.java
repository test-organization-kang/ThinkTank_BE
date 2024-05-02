package com.thinktank.global.common.util;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

	private static final int COOKIE_MAX_AGE = 24 * 60 * 60;

	public static Cookie generateRefreshTokenCookie(String refreshTokenName, String token) {
		Cookie refreshTokenCookie = new Cookie(refreshTokenName, token);
		refreshTokenCookie.setMaxAge(COOKIE_MAX_AGE);
		refreshTokenCookie.setHttpOnly(true);

		return refreshTokenCookie;
	}

	public static String getCookieValue(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = Optional.ofNullable(request.getCookies())
			.orElse(new Cookie[0]);

		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(cookieName))
			.findFirst()
			.map(Cookie::getValue)
			.orElse(null);
	}

	public static Cookie expireRefreshTokenCookie(String refreshTokenName) {
		Cookie refreshTokenCookie = new Cookie(refreshTokenName, null);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setMaxAge(0);

		return refreshTokenCookie;
	}
}