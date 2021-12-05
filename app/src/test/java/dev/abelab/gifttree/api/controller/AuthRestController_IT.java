package dev.abelab.gifttree.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import mockit.Expectations;
import mockit.Mocked;

import dev.abelab.gifttree.api.request.LoginRequest;
import dev.abelab.gifttree.api.request.SignUpRequest;
import dev.abelab.gifttree.api.response.AccessTokenResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.db.entity.UserExample;
import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.model.FileModel;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.BaseException;
import dev.abelab.gifttree.exception.BadRequestException;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * AuthRestController Integration Test
 */
public class AuthRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String LOGIN_PATH = BASE_PATH + "/login";
	static final String SIGNUP_PATH = BASE_PATH + "/signup";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserMapper userMapper;

	@Mocked
	CloudStorageClient cloudStorageClient;

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
			final var request = postRequest(LOGIN_PATH, requestBody);
			final var response = execute(request, HttpStatus.OK, AccessTokenResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getAccessToken()).isNotNull();
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
			final var request = postRequest(LOGIN_PATH, requestBody);
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
			final var request = postRequest(LOGIN_PATH, requestBody);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

	}

	/**
	 * サインアップAPIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class SignUp_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザがサインアップ() throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			final var requestBody = modelMapper.map(user, SignUpRequest.class);

			final var iconUrl = "http://example.com/test_file.jpg";
			new Expectations() {
				{
					cloudStorageClient.uploadFile((FileModel) any);
					result = iconUrl;
				}
			};

			/*
			 * test
			 */
			final var request = postRequest(SIGNUP_PATH, requestBody);
			final var response = execute(request, HttpStatus.CREATED, AccessTokenResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getAccessToken()).isNotNull();
			assertThat(response.getTokenType()).isEqualTo("Bearer");

			final var createdUser = userMapper.selectByExample(new UserExample() {
				{
					createCriteria().andEmailEqualTo(user.getEmail());
				}
			}).stream().findFirst();
			assertThat(createdUser.isPresent()).isTrue();
			assertThat(createdUser.get()) //
				.extracting(User::getEmail, User::getName) //
				.containsExactly(user.getEmail(), user.getName());
			assertThat(passwordEncoder.matches(requestBody.getPassword(), createdUser.get().getPassword())).isTrue();
			assertThat(createdUser.get().getIconUrl()).isEqualTo(iconUrl);
		}

		@Test
		void 正_アイコンがNULLの時はアイコンURLもNULLになる() throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			final var requestBody = modelMapper.map(user, SignUpRequest.class);
			requestBody.setIcon(null);

			/*
			 * test
			 */
			final var request = postRequest(SIGNUP_PATH, requestBody);
			execute(request, HttpStatus.CREATED, AccessTokenResponse.class);

			/*
			 * verify
			 */
			final var createdUser = userMapper.selectByExample(new UserExample() {
				{
					createCriteria().andEmailEqualTo(user.getEmail());
				}
			}).stream().findFirst();
			assertThat(createdUser.isPresent()).isTrue();
			assertThat(createdUser.get().getIconUrl()).isNull();
		}

		@Test
		void 異_メールアドレスが既に存在する() throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().password(LOGIN_USER_PASSWORD).build();
			userMapper.insert(user);

			final var requestBody = modelMapper.map(user, SignUpRequest.class);

			/*
			 * test & verify
			 */
			final var request = postRequest(SIGNUP_PATH, requestBody);
			execute(request, new ConflictException(ErrorCode.CONFLICT_EMAIL));
		}

		@ParameterizedTest
		@MethodSource
		void パスワードが有効かチェック(final String password, final BaseException expectedException) throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().password(password).build();
			final var requestBody = modelMapper.map(user, SignUpRequest.class);

			/*
			 * test & verify
			 */
			final var request = postRequest(SIGNUP_PATH, requestBody);
			if (Objects.isNull(expectedException)) {
				execute(request, HttpStatus.CREATED);
			} else {
				execute(request, expectedException);
			}
		}

		Stream<Arguments> パスワードが有効かチェック() {
			return Stream.of( // パスワード、期待される例外
				// 有効
				arguments("f4BabxEr", null), //
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdA", null), //
				// 無効：8文字以下
				arguments("f4BabxE", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効：33文字以上
				arguments("f4BabxEr4gNsjdtRpH9Pfs6Atth9bqdAN", new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE)), //
				// 無効：大文字を含まない
				arguments("f4babxer", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効：小文字を含まない
				arguments("F4BABXER", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)), //
				// 無効：数字を含まない
				arguments("fxbabxEr", new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD)) //
			);
		}

	}

}
