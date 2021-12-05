package dev.abelab.gifttree.api.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.api.response.UsersResponse;
import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.helper.util.RandomUtil;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;
	static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserMapper userMapper;

	/**
	 * ログインユーザ詳細取得APIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLoginUser_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザの詳細を取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			/*
			 * test
			 */
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UserResponse.class);

			/*
			 * verify
			 */
			assertThat(response) //
				.extracting(UserResponse::getId, UserResponse::getEmail, UserResponse::getName) //
				.containsExactly(loginUser.getId(), loginUser.getEmail(), loginUser.getName());
			assertThat(response.getIconUrl()).isNotNull();
		}

		@Test
		void 異_存在しないユーザの場合は取得不可() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(false);
			final var credentials = getLoginUserCredentials(loginUser);

			/*
			 * test & verify
			 */
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ユーザ一覧取得APIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsers_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザ一覧を取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var users = Arrays.asList( //
				loginUser, //
				UserSample.builder().email(RandomUtil.generateEmail()).build(), //
				UserSample.builder().email(RandomUtil.generateEmail()).build() //
			);
			userMapper.insert(users.get(1));
			userMapper.insert(users.get(2));

			/*
			 * test
			 */
			final var request = getRequest(GET_USERS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, UsersResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getUsers()) //
				.extracting(UserResponse::getId, UserResponse::getEmail, UserResponse::getName) //
				.containsExactlyElementsOf(
					users.stream().map(user -> tuple(user.getId(), user.getEmail(), user.getName())).collect(Collectors.toList()));
			response.getUsers().forEach(user -> assertThat(user.getIconUrl()).isNotNull());
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_USERS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
