package me.jjh.mapia.webserver.dto.mail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.ComDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.dto.mail
 * fileName       : MailTplDTO
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
public class MailTplDTO extends ComDTO {
    private Long mailTplIdx; // idx
    private String mailTplKey; // 메일 키
    private String mailTplTitle; // 메일 제목
    private String mailTplContent; // 메일 내용

    private Map<String, String> variables; // 템플릿 치환 변수
}
