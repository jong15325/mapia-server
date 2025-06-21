package me.jjh.mapia.webserver.dto.mail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.dto.mail
 * fileName       : MailDTO
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class MailDTO extends ComDTO {

    private Long mailIdx;          // 메일 ID
    private String mailSndr;     // 발신자 이메일
    private String mailRecvType; // 수신 타입(TO, CC, BCC)
    private String mailRecv;  // 수신자
    private String mailTitle; // 메일 제목
    private String mailContent; // 메일 내용
    private String mailStatus;     // 메일 상태 (WAIT, SUCCESS, FAIL)

    private List<String> mailRecvList;  // TO 수신자 리스트
    private List<String> mailCcList;  // CC 참조자 리스트
    private List<String> mailBccList; // BCC 숨은참조자 리스트
    private MailTplDTO mailTpl; // 템플릿

}
