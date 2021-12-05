package dev.abelab.gifttree.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abelab.gifttree.property.JwtProperty;
import dev.abelab.gifttree.helper.sample.UserSample;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.BaseException;
import dev.abelab.gifttree.exception.BadRequestException;
import dev.abelab.gifttree.exception.UnauthorizedException;

public class AuthUtil_UT extends AbstractUtil_UT {

    @Injectable
    PasswordEncoder passwordEncoder;

    @Injectable
    JwtProperty jwtProperty;

    @Tested
    AuthUtil authUtil;

    /**
     * AuthUtil::generateCredentials UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class GenerateCredentials_UT {

        @Test
        void 正_ユーザのクレデンシャルを発行() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    jwtProperty.getIssuer();
                    result = SAMPLE_STR;
                }
                {
                    jwtProperty.getSecret();
                    result = SAMPLE_STR;
                }
                {
                    jwtProperty.getExpiredIn();
                    result = SAMPLE_INT;
                }
            };

            /*
             * test & verify
             */
            final var credentials = authUtil.generateCredentials(user);
            assertThat(credentials).isNotBlank();
        }

    }

    /**
     * AuthUtil::verifyCredentials UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class VerifyCredentials_UT {

        @Test
        void 異_無効なクレデンシャル() throws Exception {
            /*
             * given
             */
            final var credentials = "";

            new Expectations() {
                {
                    jwtProperty.getSecret();
                    result = SAMPLE_STR;
                }
            };

            /*
             * test & verify
             */
            final var exception = assertThrows(UnauthorizedException.class, () -> authUtil.verifyCredentials(credentials));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    /**
     * AuthUtil::verifyPassword UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class VerifyPassword_UT {

        @Test
        void 正_パスワードが一致している() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    passwordEncoder.matches(anyString, anyString);
                    result = true;
                }
            };

            /*
             * test & verify
             */
            assertDoesNotThrow(() -> authUtil.verifyPassword(user, anyString()));
        }

        @Test
        void 異_パスワードが一致しない() throws Exception {
            /*
             * given
             */
            final var user = UserSample.builder().build();

            new Expectations() {
                {
                    passwordEncoder.matches(anyString, anyString);
                    result = false;
                }
            };

            /*
             * test & verify
             */
            final var exception = assertThrows(UnauthorizedException.class, () -> authUtil.verifyPassword(user, anyString()));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_PASSWORD);
        }

    }

    /**
     * AuthUtil::validatePassword UT
     */
    @Nested
    @TestInstance(PER_CLASS)
    public class ValidatePassword_UT {

        @ParameterizedTest
        @MethodSource
        void 有効なパスワードかチェック(final String password, final BaseException expectedException) {
            /*
             * test & verify
             */
            if (Objects.isNull(expectedException)) {
                assertDoesNotThrow(() -> authUtil.validatePassword(password));
            } else {
                final var occurredException = assertThrows(expectedException.getClass(), () -> authUtil.validatePassword(password));
                assertThat(occurredException.getErrorCode()).isEqualTo(expectedException.getErrorCode());
            }
        }

        Stream<Arguments> 有効なパスワードかチェック() {
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
