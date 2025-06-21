package me.jjh.mapia.webserver.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

/**
 * packageName    : me.jjh.mapia.dto.member
 * fileName       : MemberFile
 * author         : JJH
 * date           : 2025-04-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-04-18        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class MemberFileDTO extends ComDTO {

    private Long fileIdx;             // 파일 인덱스
    private Long memberIdx;            // 멤버 인덱스
    private String orgFileName;  // 원본 파일명
    private String savedFileName;     // 저장된 파일명
    private String fileType;  // 파일 타입
    private String filePath;          // 파일 경로
    private String fileExtension;     // 파일 확장자
    private Long fileSize;            // 파일 크기
    private String fileContentType;   // 파일 MIME 타입
    private Integer fileOrder;        // 파일 순서
    private String fileKey; // 파일 키
    private String isDeleted;         // 삭제 여부
}
