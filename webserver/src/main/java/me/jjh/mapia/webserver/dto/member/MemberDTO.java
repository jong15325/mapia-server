package me.jjh.mapia.webserver.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MemberDTO extends ComDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7298347L;

    private Long memberIdx;   // Primary Key
    private String memberId; // 아이디(이메일 주소)
    private String memberProfile;
    private String memberLoginIp; // 아이피
    private int memberLoginCnt;
    private LocalDateTime memberLastLogin; // 마지막 로그인일
    private Long memberRoleIdx;  // 역할 idx
    private Long memberDeptIdx; //부서 idx
    private String isActive;   // 활성화 여부
    private String isDeleted;   // 삭제 여부

    // 권한 및 역할
    private RoleDTO role;
    // 회원 인증 정보 목록
    private List<MemberAuthDTO> memberAuths;

}
