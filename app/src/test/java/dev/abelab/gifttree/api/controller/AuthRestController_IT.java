package dev.abelab.gifttree.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import dev.abelab.gifttree.api.request.LoginRequest;
import dev.abelab.gifttree.api.response.AccessTokenResponse;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * AuthRestController Integration Test
 */
public class AuthRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String LOGIN_PATH = BASE_PATH + "/login";

	/**
	 * ログインAPIのIT
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class Login_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザがログイン() throws Exception {
			/*
			 * given
			 */
			createLoginUser();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD) //
				.build();

			/*
			 * test
			 */
			final var request = postRequest("/api/login", requestBody);
			final var response = execute(request, HttpStatus.OK, AccessTokenResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getAccessToken().length()).isEqualTo(255);
			assertThat(response.getTokenType()).isEqualTo("Bearer");
		}

		@Test
		void 異_存在しないユーザがログイン() throws Exception {
			/*
			 * given
			 */
			final var loginUser = UserSample.builder().build();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(loginUser.getEmail()) //
				.password(loginUser.getPassword()) //
				.build();

			/*
			 * test & verify
			 */
			final var request = postRequest("/api/login", requestBody);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_パスワードが間違えている() throws Exception {
			/*
			 * given
			 */
			createLoginUser();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD + "dummy") //
				.build();

			/*
			 * test & verify
			 */
			final var request = postRequest("/api/login", requestBody);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

	}

}
