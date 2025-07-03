package me.jjh.mapia.webserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.jjh.mapia.webserver.common.code.RedisKeyCode;
import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import me.jjh.mapia.webserver.properties.CryptoProperties;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    private void init() {
        SecurityUtil.secretKey = cryptoProperties.getSecretKey();
        SecurityUtil.jwtSecretKey = cryptoProperties.getJwtSecretKey();
    }

    /**
     * 선택 가능한 자릿수 인증 코드 생성 (숫자)
     * @param length 인증 코드 자릿수 (예: 4, 6, 8 등)
     */
    public static String generateNumberCode(int length) {
        if (length < 4 || length > 10) {
            throw new IllegalArgumentException("인증 코드는 최소 4자리에서 최대 10자리까지 가능합니다.");
        }

        int minValue = (int) Math.pow(10, length - 1);
        int maxValue = (int) Math.pow(10, length) - 1;
        return java.lang.String.valueOf(ThreadLocalRandom.current().nextInt(minValue, maxValue));
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

    /**
     * 파라미터 문자열 암호화
     */
    public static String paramEncrypt(String text) {
        if (text == null || text.isEmpty()) return "";

        try {
            Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    /**
     * 파라미터 문자열 복호화
     */
    public static String paramDecrypt(String encrypted) {
        if (encrypted == null || encrypted.isEmpty()) return "";

        try {
            Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] original = cipher.doFinal(Base64.getUrlDecoder().decode(encrypted));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }

    /**
     * 객체를 JSON으로 변환 후 암호화
     */
    public static String paramEncryptObject(Object obj) {
        try {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
            return paramEncrypt(json);
        } catch (Exception e) {
            throw new RuntimeException("객체 암호화 실패", e);
        }
    }

    /**
     * 암호화된 문자열을 JSON으로 복호화 후 객체로 변환
     */
    public static <T> T paramDecryptObject(String encrypted, Class<T> valueType) {
        try {
            String json = paramDecrypt(encrypted);
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException("객체 복호화 실패", e);
        }
    }

    /**
     * 비밀번호 암호화
     */
    public static String encryptPassword(String rawPassword) {
        if (rawPassword == null || DataVaildUtil.isStringEmpty(rawPassword)) {
            throw new IllegalArgumentException("변환할 비밀번호가 없습니다");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 입력한 비밀번호와 암호화된 비밀번호 비교
     */
    public static boolean matches(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || encryptedPassword == null || DataVaildUtil.isStringEmpty(rawPassword)) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    /**
     * 파일 키 생성 - 파일 전용
     */
    public static String generateFileKey(Long idx, String fileType, Long fileSize, Long createdIdx) {

        // 현재 시간
        long timestamp = System.currentTimeMillis();

        // 키 생성을 위한 데이터 조합
        StringBuilder rawKey = new StringBuilder();
        rawKey.append(idx).append("_")
                .append(fileType != null ? fileType : "unknown").append("_")
                .append(fileSize != null ? fileSize : 0).append("_")
                .append(createdIdx != null ? createdIdx : 0).append("_")
                .append(timestamp).append("_")
                .append(secretKey); // 서버의 시크릿 키 추가

        try {
            // SHA-256 해시 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.toString().getBytes(StandardCharsets.UTF_8));

            // URL 안전한 Base64 인코딩 (파일 다운로드 URL에 사용하기 적합)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    /**
     * 게임서버 접속용 JWT 토큰 생성
     * @param member 인증된 사용자 정보
     * @return JWT 토큰
     */
    public static String generateGameAccessToken(MemberResDTO member) {
        Date now = new Date();
        Duration tokenDuration = RedisKeyCode.GAME_SOCKET_ACCESS.getDuration();
        Date expAt = new Date(now.getTime() + tokenDuration.toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("memberIdx", member.getMemberIdx());
        claims.put("memberId", member.getMemberId());
        claims.put("tokenType", RedisKeyCode.GAME_SOCKET_ACCESS.getType());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getMemberId())
                .setIssuedAt(now)
                .setExpiration(expAt)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

}
