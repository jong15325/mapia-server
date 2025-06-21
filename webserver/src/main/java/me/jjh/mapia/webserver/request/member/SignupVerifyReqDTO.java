package me.jjh.mapia.webserver.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : me.jjh.mapia.request.member
 * fileName       : SignupVerifyReqDTO
 * author         : JJH
 * date           : 2025-03-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-24        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class SignupVerifyReqDTO {

    private String verifyToken;

    @Size(min = 4, message = "인증 코드는 4자리입니다.")
    private String verifyCode;

}
