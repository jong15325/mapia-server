package me.jjh.mapia.webserver.dto.verify;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * packageName    : me.jjh.mapia.dto.verify
 * fileName       : VerfiyRedisDTO
 * author         : JJH
 * date           : 2025-03-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-07        JJH       최초 생성
 */


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyDTO extends ComDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 66023944L;

    private Long verifyIdx;  // 인증 idx
    private String verifyType; // 인증타입
    private String verifyTarget; // 인증 타겟(멤버 아이디, 전화번호 등)
    private String verifyToken; // 인증 토큰
    private String verifyCode; // 인증 코드
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verifyIssuedAt; // 발급 시간
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verifyExpAt; // 만료 시간
    private String isActive; // 만료시간
    private String verifyIp; // 아이피
    private int verifyCnt; // 인증 횟수
    private String verifyAgent; // 인증 기기 정보
}
