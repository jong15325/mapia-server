package me.jjh.mapia.webserver.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * packageName    : me.jjh.mapia.dto.member
 * fileName       : MemberAuthDTO
 * author         : JJH
 * date           : 2025-03-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-23        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class MemberAuthDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 204123444L;

    private Long memberAuthIdx;
    private Long memberIdx;
    private String memberAuthProvider;
    private String memberAuthProviderId;
    private String memberAuthPassword;
    private String memberAuthAccessToken;
    private String memberAuthRefreshToken;
    private LocalDateTime memberAuthExpAt;
    private String memberAuthIsPrimary;
    private LocalDateTime memberAuthLastLogin;
}
