package dev.abelab.gifttree.api.controller;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import dev.abelab.gifttree.api.response.GiftResponse;
import dev.abelab.gifttree.api.response.GiftsResponse;
import dev.abelab.gifttree.api.request.GiftShareRequest;
import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.db.mapper.GiftMapper;
import dev.abelab.gifttree.db.mapper.UserGiftMapper;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.enums.UserGiftTypeEnum;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.helper.sample.GiftSample;
import dev.abelab.gifttree.helper.sample.UserGiftSample;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * GiftRestController Integration Test
 */
public class GiftRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String OBTAIN_GIFT_PATH = BASE_PATH + "/gifts/%d/obtain";
	static final String GET_LOGIN_USER_GIFTS_PATH = BASE_PATH + "/users/me/gifts";
	static final String SHARE_GIFT_PATH = BASE_PATH + "/users/%d/gifts";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	GiftMapper giftMapper;

	@Autowired
	UserGiftMapper userGiftMapper;

	/**
	 * ギフト受け取りAPIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class ObtainGift_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ギフトを受け取る() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			/*
			 * test
			 */
			final var request = postRequest(String.format(OBTAIN_GIFT_PATH, gift.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.CREATED);

			/*
			 * verify
			 */
			final var createdUserGift = userGiftMapper.selectByPrimaryKey(loginUser.getId(), gift.getId());
			assertThat(createdUserGift).extracting(UserGift::getType, UserGift::getQuantity, UserGift::getReceivedBy) //
				.contains(UserGiftTypeEnum.STORE.getId(), gift.getQuantity(), null);
		}

		@Test
		void 異_受け取り済みのギフトは受け取れない() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			/*
			 * test & verify
			 */
			final var request = postRequest(String.format(OBTAIN_GIFT_PATH, gift.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.CREATED);
			execute(request, new ConflictException(ErrorCode.GIFT_ALREADY_OBTAINED));
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = postRequest(String.format(OBTAIN_GIFT_PATH, SAMPLE_INT));
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * 所持ギフト一覧取得APIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLoginUserGifts_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ユーザの所持ギフト一覧を取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gifts = Arrays.asList( //
				GiftSample.builder().quantity(10).build(), //
				GiftSample.builder().quantity(20).build(), //
				GiftSample.builder().quantity(30).build() //
			);
			gifts.forEach(giftMapper::insert);

			final var userGifts = Arrays.asList( //
				UserGiftSample.builder().giftId(gifts.get(0).getId()).userId(loginUser.getId()).type(UserGiftTypeEnum.STORE.getId())
					.quantity(gifts.get(0).getQuantity()).build(), //
				UserGiftSample.builder().giftId(gifts.get(1).getId()).userId(loginUser.getId()).type(UserGiftTypeEnum.STORE.getId())
					.quantity(gifts.get(1).getQuantity()).build() //
			);
			userGifts.forEach(userGiftMapper::insert);

			/*
			 * test
			 */
			final var request = getRequest(GET_LOGIN_USER_GIFTS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, GiftsResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getGifts())
				.extracting(GiftResponse::getId, GiftResponse::getName, GiftResponse::getDescription, GiftResponse::getQuantity,
					GiftResponse::getUrl) //
				.containsExactlyInAnyOrder( //
					tuple(gifts.get(0).getId(), gifts.get(0).getName(), gifts.get(0).getDescription(), gifts.get(0).getQuantity(),
						gifts.get(0).getUrl()), //
					tuple(gifts.get(1).getId(), gifts.get(1).getName(), gifts.get(1).getDescription(), gifts.get(1).getQuantity(),
						gifts.get(1).getUrl()) //
				);
		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_LOGIN_USER_GIFTS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

	/**
	 * ギフトおすそわけAPIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class ShareGift_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ギフトをおすそわけ() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			final var loginUserGift = UserGiftSample.builder().userId(loginUser.getId()).giftId(gift.getId()).quantity(10).build();
			userGiftMapper.insert(loginUserGift);

			final var user = UserSample.builder().build();
			userMapper.insert(user);

			final var requestBody = GiftShareRequest.builder() //
				.giftId(gift.getId()) //
				.build();

			/*
			 * test
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, user.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			/*
			 * verify
			 */
			final var updatedLoginUserGift = userGiftMapper.selectByPrimaryKey(loginUser.getId(), gift.getId());
			assertThat(updatedLoginUserGift.getQuantity()).isEqualTo(9);
			final var createdUserGift = userGiftMapper.selectByPrimaryKey(user.getId(), gift.getId());
			assertThat(createdUserGift).extracting(UserGift::getType, UserGift::getQuantity, UserGift::getReceivedBy) //
				.containsExactly(UserGiftTypeEnum.FRIEND.getId(), 1, loginUser.getId());
		}

		@Test
		void 正_ギフトの残りが1の場合_削除される() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			final var loginUserGift = UserGiftSample.builder().userId(loginUser.getId()).giftId(gift.getId()).quantity(1).build();
			userGiftMapper.insert(loginUserGift);

			final var user = UserSample.builder().build();
			userMapper.insert(user);

			final var requestBody = GiftShareRequest.builder() //
				.giftId(gift.getId()) //
				.build();

			/*
			 * test
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, user.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			/*
			 * verify
			 */
			final var updatedLoginUserGift = userGiftMapper.selectByPrimaryKey(loginUser.getId(), gift.getId());
			assertThat(updatedLoginUserGift).isNull();
		}

		@Test
		void 正_相手が既にギフトを所持していた場合_1加算される() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var user = UserSample.builder().build();
			userMapper.insert(user);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			final var userGifts = Arrays.asList( //
				UserGiftSample.builder().userId(loginUser.getId()).giftId(gift.getId()).build(), //
				UserGiftSample.builder().userId(user.getId()).giftId(gift.getId()).build() //
			);
			userGifts.forEach(userGiftMapper::insert);

			final var requestBody = GiftShareRequest.builder() //
				.giftId(gift.getId()) //
				.build();

			/*
			 * test
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, user.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			/*
			 * verify
			 */
			final var updatedUserGift = userGiftMapper.selectByPrimaryKey(user.getId(), gift.getId());
			assertThat(updatedUserGift.getQuantity()).isEqualTo(userGifts.get(0).getQuantity() + 1);
		}

		@Test
		void 異_おすそわけする相手ユーザが存在しない() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			final var loginUserGift = UserGiftSample.builder().userId(loginUser.getId()).giftId(gift.getId()).quantity(10).build();
			userGiftMapper.insert(loginUserGift);

			final var requestBody = GiftShareRequest.builder() //
				.giftId(gift.getId()) //
				.build();

			/*
			 * test & verify
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, loginUser.getId() + 1), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_ギフトを所持していない() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var gift = GiftSample.builder().build();
			giftMapper.insert(gift);

			final var user = UserSample.builder().build();
			userMapper.insert(user);

			final var requestBody = GiftShareRequest.builder() //
				.giftId(gift.getId()) //
				.build();

			/*
			 * test & verify
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, user.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER_GIFT));

		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * given
			 */
			final var requestBody = GiftShareRequest.builder() //
				.giftId(SAMPLE_INT) //
				.build();

			/*
			 * test & verify
			 */
			final var request = postRequest(String.format(SHARE_GIFT_PATH, SAMPLE_INT), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
