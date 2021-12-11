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

import dev.abelab.gifttree.api.response.RankingResponse;
import dev.abelab.gifttree.db.mapper.UserMapper;
import dev.abelab.gifttree.db.mapper.GiftMapper;
import dev.abelab.gifttree.db.mapper.UserGiftMapper;
import dev.abelab.gifttree.db.mapper.NotificationMapper;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.helper.sample.NotificationSample;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.UnauthorizedException;

/**
 * RankingRestController Integration Test
 */
public class RankingRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/ranking";
	static final String GET_RANKING_PATH = BASE_PATH;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	GiftMapper giftMapper;

	@Autowired
	NotificationMapper notificationMapper;

	@Autowired
	UserGiftMapper userGiftMapper;

	/**
	 * ランキング取得APIの統合テスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetRanking_IT extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ランキングを取得() throws Exception {
			/*
			 * given
			 */
			final var loginUser = createLoginUser(true);
			final var credentials = getLoginUserCredentials(loginUser);

			final var user1 = UserSample.builder().email("user1").build();
			final var user2 = UserSample.builder().email("user2").build();
			final var user3 = UserSample.builder().email("user3").build();
			userMapper.insert(user1);
			userMapper.insert(user2);
			userMapper.insert(user3);

			// 2位
			final var user1Notifications = Arrays.asList( //
				NotificationSample.builder().userId(user1.getId()).build(), //
				NotificationSample.builder().userId(user1.getId()).build(), //
				NotificationSample.builder().userId(user1.getId()).build() //
			);
			user1Notifications.forEach(notificationMapper::insert);

			// 3位
			final var user2Notifications = Arrays.asList( //
				NotificationSample.builder().userId(user2.getId()).build() //
			);
			user2Notifications.forEach(notificationMapper::insert);

			// 1位
			final var user3Notifications = Arrays.asList( //
				NotificationSample.builder().userId(user3.getId()).build(), //
				NotificationSample.builder().userId(user3.getId()).build(), //
				NotificationSample.builder().userId(user3.getId()).build(), //
				NotificationSample.builder().userId(user3.getId()).build() //
			);
			user3Notifications.forEach(notificationMapper::insert);

			/*
			 * test
			 */
			final var request = getRequest(GET_RANKING_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			final var response = execute(request, HttpStatus.OK, RankingResponse.class);

			/*
			 * verify
			 */
			assertThat(response.getUserRanks().size()).isEqualTo(4);

			// 1位
			assertThat(response.getUserRanks().get(0).getRank()).isEqualTo(1);
			assertThat(response.getUserRanks().get(0).getScore()).isEqualTo(user3Notifications.size());
			assertThat(response.getUserRanks().get(0).getUser().getId()).isEqualTo(user3.getId());
			// 2位
			assertThat(response.getUserRanks().get(1).getRank()).isEqualTo(2);
			assertThat(response.getUserRanks().get(1).getScore()).isEqualTo(user1Notifications.size());
			assertThat(response.getUserRanks().get(1).getUser().getId()).isEqualTo(user1.getId());
			// 3位
			assertThat(response.getUserRanks().get(2).getRank()).isEqualTo(3);
			assertThat(response.getUserRanks().get(2).getScore()).isEqualTo(user2Notifications.size());
			assertThat(response.getUserRanks().get(2).getUser().getId()).isEqualTo(user2.getId());
			// 4位
			assertThat(response.getUserRanks().get(3).getRank()).isEqualTo(4);
			assertThat(response.getUserRanks().get(3).getScore()).isEqualTo(0);
			assertThat(response.getUserRanks().get(3).getUser().getId()).isEqualTo(loginUser.getId());

		}

		@Test
		void 異_無効な認証ヘッダ() throws Exception {
			/*
			 * test & verify
			 */
			final var request = getRequest(GET_RANKING_PATH);
			request.header(HttpHeaders.AUTHORIZATION, "");
			execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
		}

	}

}
