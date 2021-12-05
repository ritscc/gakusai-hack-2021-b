package dev.abelab.gifttree.service;

import static org.junit.jupiter.api.Assertions.*;
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
import org.modelmapper.ModelMapper;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.util.AuthUtil;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.BaseException;
import dev.abelab.gifttree.exception.UnauthorizedException;

public class AuthService_UT extends AbstractService_UT {

	@Injectable
	ModelMapper modelMapper;

	@Injectable
	UserRepository userRepository;

	@Injectable
	AuthUtil authUtil;

	@Injectable
	CloudStorageClient cloudStorageClient;

	@Tested
	AuthService authService;

	/**
	 * AuthService::getLoginUser UT
	 */
	@Nested
	@TestInstance(PER_CLASS)
	public class GetLoginUser_UT {

		@ParameterizedTest
		@MethodSource
		void クレデンシャルの構文チェック(final String credentials, final BaseException expectedException) throws Exception {
			/*
			 * given
			 */
			new Expectations() {
				{
					authUtil.verifyCredentials(anyString);
					result = SAMPLE_INT;
					minTimes = 0;
				}
				{
					userRepository.selectById(anyInt);
					result = UserSample.builder().build();
					minTimes = 0;
				}
			};

			/*
			 * test & verify
			 */
			if (Objects.isNull(expectedException)) {
				assertDoesNotThrow(() -> authService.getLoginUser(credentials));
			} else {
				assertThrows(expectedException.getClass(), () -> authService.getLoginUser(credentials));
			}
		}

		Stream<Arguments> クレデンシャルの構文チェック() {
			return Stream.of( // クレデンシャル、期待される例外
				// 有効
				arguments("Bearer xxx", null), //
				// 無効
				arguments("xxx", new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN)) //
			);
		}

	}

}
