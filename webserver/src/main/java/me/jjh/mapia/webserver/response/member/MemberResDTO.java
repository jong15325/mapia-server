package me.jjh.mapia.webserver.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RoleDTO;

import java.time.LocalDateTime;

/**
 * packageName    : me.jjh.mapia.reponse.member
 * fileName       : MemberResponse
 * author         : JJH
 * date           : 2025-02-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-22        JJH       최초 생성
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDTO {

    private Long memberIdx;   // Primary Key
    private String memberId; // 아이디(이메일 주소)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime memberLastLogin; // 마지막 로그인일
    private Long memberRoleIdx;  // 역할 idx
    private Long memberDeptIdx; // 부서 idx
    private String isActive;   // 계정 활성화 여부
    private String isDeleted;   // 계정 삭제 여부
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 생성일
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일
    private RoleDTO role;  // 역할 정보

    public static MemberResDTO fromDTO(MemberDTO memberDTO) {
        if(memberDTO == null) {
            return null;
        }

        return new MemberResDTO(
                memberDTO.getMemberIdx(),
                memberDTO.getMemberId(),
                memberDTO.getMemberLastLogin(),
                memberDTO.getMemberRoleIdx(),
                memberDTO.getMemberDeptIdx(),
                memberDTO.getIsActive(),
                memberDTO.getIsDeleted(),
                memberDTO.getCreatedAt(),
                memberDTO.getUpdatedAt(),
                memberDTO.getRole()
        );
    }
}
