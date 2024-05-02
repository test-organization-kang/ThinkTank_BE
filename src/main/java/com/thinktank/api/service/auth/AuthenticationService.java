package com.thinktank.api.service.auth;

import static com.thinktank.global.common.util.AuthConstants.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinktank.api.dto.user.request.LoginReqDto;
import com.thinktank.api.dto.user.response.LoginResDto;
import com.thinktank.api.entity.User;
import com.thinktank.api.entity.auth.AuthUser;
import com.thinktank.api.repository.UserRepository;
import com.thinktank.global.common.util.CookieUtils;
import com.thinktank.global.error.exception.BadRequestException;
import com.thinktank.global.error.exception.NotFoundException;
import com.thinktank.global.error.exception.UnauthorizedException;
import com.thinktank.global.error.model.ErrorCode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final JwtProviderService jwtProviderService;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public LoginResDto login(LoginReqDto loginReqDto, HttpServletResponse response) {
		final User user = findByUserEmail(loginReqDto.email());

		validatePasswordMatch(loginReqDto.password(), user.getPassword());

		final String accessToken = jwtProviderService.generateAccessToken(user.getEmail(), user.getNickname());
		final String refreshToken = jwtProviderService.generateRefreshToken(user.getEmail());
		user.updateRefreshToken(refreshToken);

		response.setHeader(ACCESS_TOKEN_HEADER, accessToken);

		Cookie refreshTokenCookie = CookieUtils.generateRefreshTokenCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
		response.addCookie(refreshTokenCookie);

		return new LoginResDto(accessToken, refreshToken);
	}

	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtProviderService.extractRefreshToken(REFRESH_TOKEN_COOKIE_NAME, request);

		final AuthUser authUser = jwtProviderService.extractAuthUserByAccessToken(refreshToken);
		validateRefreshToken(authUser);

		final User user = findByUserEmail(authUser.email());
		user.updateRefreshToken(null);

		Cookie refreshTokenCookie = CookieUtils.expireRefreshTokenCookie(REFRESH_TOKEN_COOKIE_NAME);
		response.addCookie(refreshTokenCookie);
	}

	private User findByUserEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.FAIL_NOT_USER_FOUND_EXCEPTION));
	}

	private void validatePasswordMatch(String password, String encodedPassword) {
		if (!passwordEncoder.matches(password, encodedPassword)) {
			throw new BadRequestException(ErrorCode.FAIL_WRONG_PASSWORD);
		}
	}

	private void validateRefreshToken(AuthUser authUser) {
		if (authUser == null) {
			throw new UnauthorizedException(ErrorCode.FAIL_INVALID_TOKEN_EXCEPTION);
		}
	}
}