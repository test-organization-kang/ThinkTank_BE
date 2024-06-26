package com.thinktank.global.auth;

import com.thinktank.api.entity.auth.AuthUser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorizationThreadLocal {

	private static final ThreadLocal<AuthUser> authUser;

	static {
		authUser = new ThreadLocal<>();
	}

	public static void setAuthUser(AuthUser authUser) {
		AuthorizationThreadLocal.authUser.set(authUser);
	}

	public static AuthUser getAuthUser() {
		return authUser.get();
	}

	public static void remove() {
		authUser.remove();
	}
}
