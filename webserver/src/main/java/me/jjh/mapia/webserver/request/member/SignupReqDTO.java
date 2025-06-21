package me.jjh.mapia.webserver.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * packageName    : me.jjh.mapia.request
 * fileName       : SignupReqDTO
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
public class SignupReqDTO {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식이 잘못되었습니다"
    )
    private String memberId; // 아이디(이메일 주소)

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
            message = "비밀번호는 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String memberPwd; // 비밀번호
}
