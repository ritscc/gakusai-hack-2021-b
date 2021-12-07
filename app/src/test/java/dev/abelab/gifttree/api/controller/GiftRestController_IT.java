package dev.abelab.gifttree.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import dev.abelab.gifttree.api.request.LoginUserUpdateRequest;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.db.entity.UserExample;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.helper.sample.GiftSample;
import dev.abelab.gifttree.helper.util.RandomUtil;
import mockit.Mocked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.db.mapper.GiftMapper;
import dev.abelab.gifttree.db.mapper.UserGiftMapper;
import dev.abelab.gifttree.enums.UserGiftTypeEnum;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * GiftRestController Integration Test
 */
public class GiftRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String OBTAIN_GIFT_PATH = BASE_PATH + "/gifts/%d/obtain";

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

}
