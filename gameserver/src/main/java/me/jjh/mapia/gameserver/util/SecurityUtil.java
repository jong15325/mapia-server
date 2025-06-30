package me.jjh.mapia.gameserver.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.jjh.mapia.gameserver.code.RedisKeyCode;
import me.jjh.mapia.gameserver.properties.CryptoProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : SecurityCodeUtil
 * author         : JJH
 * date           : 2025-02-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-26        JJH       최초 생성
 */

@Component // 초기화 시 환경변수를 가져오려면 스프링 컨텍스트에 등록되어야 함
@RequiredArgsConstructor
public class SecurityUtil {

    private static String secretKey;
    private static String jwtSecretKey;
    private final CryptoProperties cryptoProperties;
    private static final String AES_ALGORITHM = "AES";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    private void init() {
        SecurityUtil.secretKey = cryptoProperties.getSecretKey();
        SecurityUtil.jwtSecretKey = cryptoProperties.getJwtSecretKey();
    }

    /**
     * 영문 대문자 + 숫자 혼합 랜덤 인증 코드 생성
     * @param length 인증 코드 자릿수 (예: 6, 8, 10 등)
     */
    public static String generateAlphanumericCode(int length) {
        if (length < 4 || length > 10) {
            throw new IllegalArgumentException("인증 코드는 최소 4자리에서 최대 10자리까지 가능합니다.");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 인증 토큰 생성 (JWT 기반, 보안 강화)
     */
    public static String generateVerifyToken(Date verificationIssuedAt, Date verificationExpAt, RedisKeyCode verificationCode) {
        return Jwts.builder()
                .setSubject(verificationCode.getType())
                .setIssuedAt(verificationIssuedAt)
                .setExpiration(verificationExpAt)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    /**
     * 토큰 검증 및 정보 추출
     */
    public static Claims parseVerifyToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * SecretKey 변환
     */
    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
