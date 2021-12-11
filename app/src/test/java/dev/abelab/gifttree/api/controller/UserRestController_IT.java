package dev.abelab.gifttree.api.controller;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import dev.abelab.gifttree.api.request.LoginUserUpdateRequest;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.db.entity.UserExample;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.helper.sample.NotificationSample;
import dev.abelab.gifttree.helper.util.RandomUtil;
import mockit.Mocked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.api.response.NotificationsResponse;
import dev.abelab.gifttree.api.response.NotificationResponse;
import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.db.mapper.NotificationMapper;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String GET_NOTIFICATIONS_PATH = BASE_PATH + "/me/notifications";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	NotificationMapper notificationMapper;

	@Mocked
	CloudStorageClient cloudStorageClient;

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
				.extracting(UserResponse::getId, UserResponse::getName) //
				.containsExactly(loginUser.getId(), loginUser.getName());
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
	 * ログインユーザ更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUser_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザを更新() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			loginUser.setEmail(loginUser.getEmail() + "xxx");
			loginUser.setName(loginUser.getName() + "xxx");
			final var requestBody = modelMapper.map(loginUser, LoginUserUpdateRequest.class);

			/*
			 * test
			 */
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			/*
			 * verify
			 */
			final var updatedUser = userMapper.selectByExample(new UserExample() {
				{
					createCriteria().andIdEqualTo(loginUser.getId());
				}
			}).stream().findFirst();
			assertThat(updatedUser.isPresent()).isTrue();
			assertThat(updatedUser.get()) //
				.extracting(User::getEmail, User::getName) //
				.containsExactly(loginUser.getEmail(), loginUser.getName());
		}

		@Test
		void 異_更新後のメールアドレスが既に存在する() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().email(RandomUtil.generateEmail()).build();
			userMapper.insert(user);

			loginUser.setEmail(user.getEmail());
			loginUser.setName(loginUser.getName() + "xxx");
			final var requestBody = modelMapper.map(loginUser, LoginUserUpdateRequest.class);

			/*
			 * test & verify
			 */
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ConflictException(ErrorCode.CONFLICT_EMAIL));
		}

		@Test
		void 異_更新対象ユーザが存在しない() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(false);
			final var credentials = getLoginUserCredentials(loginUser);

			final var requestBody = modelMapper.map(loginUser, LoginUserUpdateRequest.class);

			/*
			 * test & verify
			 */
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * given
			 */
			final var user = UserSample.builder().build();
			final var requestBody = modelMapper.map(user, LoginUserUpdateRequest.class);

			/*
			 * test & verify
			 */
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * 通知取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetNotifications_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_通知を取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var anotherUser = UserSample.builder().email(RandomUtil.generateEmail()).build();
			userMapper.insert(anotherUser);

			final var loginUserNotifications = Arrays.asList( //
				NotificationSample.builder().userId(loginUser.getId()).build(), //
				NotificationSample.builder().userId(loginUser.getId()).build(), //
				NotificationSample.builder().userId(loginUser.getId()).build() //
			);
			loginUserNotifications.forEach(notificationMapper::insert);

			final var anotherUserNotifications = Arrays.asList( //
				NotificationSample.builder().userId(anotherUser.getId()).build(), //
				NotificationSample.builder().userId(anotherUser.getId()).build(), //
				NotificationSample.builder().userId(anotherUser.getId()).build() //
			);
			anotherUserNotifications.forEach(notificationMapper::insert);

			/*
			 * test
			 */
			final var request = getRequest(GET_NOTIFICATIONS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, NotificationsResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getNotifications().size()).isEqualTo(loginUserNotifications.size());
			assertThat(response.getNotifications()).extracting(NotificationResponse::getId, NotificationResponse::getTitle) //
				.containsExactlyInAnyOrder( //
					tuple(loginUserNotifications.get(0).getId(), loginUserNotifications.get(0).getTitle()), //
					tuple(loginUserNotifications.get(1).getId(), loginUserNotifications.get(1).getTitle()), //
					tuple(loginUserNotifications.get(2).getId(), loginUserNotifications.get(2).getTitle()) //
				);
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_NOTIFICATIONS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
