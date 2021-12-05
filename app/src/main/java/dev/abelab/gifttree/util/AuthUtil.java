package dev.abelab.gifttree.util;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.*;

import lombok.*;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.property.JwtProperty;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.BadRequestException;
import dev.abelab.gifttree.exception.UnauthorizedException;

@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final PasswordEncoder passwordEncoder;

    private final JwtProperty jwtProperty;

    /**
     * クレデンシャルを発行
     *
     * @param user ユーザ
     *
     * @return credentials
     */
    public String generateCredentials(final User user) {
        // クレームを設定
        final var claims = Jwts.claims();
        claims.put(Claims.SUBJECT, user.getId());
        claims.put(Claims.ISSUER, this.jwtProperty.getIssuer());
        claims.put(Claims.ISSUED_AT, new Date());
        claims.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + this.jwtProperty.getExpiredIn() * 1000));

        // JWTを発行
        return Jwts.builder() //
            .setClaims(claims) //
            .signWith(SignatureAlgorithm.HS512, this.jwtProperty.getSecret().getBytes()) //
            .compact();
    }

    /**
     * クレデンシャルを検証
     *
     * @param credentials クレデンシャル
     *
     * @return ユーザID
     */
    public Integer verifyCredentials(final String credentials) {
        try {
            // クレームを取得
            final var claims = Jwts.parser() //
                .setSigningKey(this.jwtProperty.getSecret().getBytes()) //
                .parseClaimsJws(credentials) //
                .getBody();

            // ユーザID
            final var userId = Optional.ofNullable((Integer) claims.get(Claims.SUBJECT));
            return userId.orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
        } catch (SignatureException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    /**
     * パスワードをエンコード
     *
     * @param password パスワード
     *
     * @return ハッシュ値
     */
    public String encodePassword(final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * パスワードを検証
     *
     * @param user     ユーザ
     * @param password パスワード
     */
    public void verifyPassword(final User user, final String password) {
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }
    }

    /**
     * パスワードのバリデーション
     *
     * @param password パスワード
     */
    public void validatePassword(final String password) {
        // 8~32文字かどうか
        if (password.length() < 8 || password.length() > 32) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE);
        }
        // 大文字・小文字・数字を含むか
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).+$")) {
            throw new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD);
        }
    }

}
