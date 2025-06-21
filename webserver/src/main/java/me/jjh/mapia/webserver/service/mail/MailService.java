package me.jjh.mapia.webserver.service.mail;

import me.jjh.mapia.webserver.common.code.EmailTplCode;
import me.jjh.mapia.webserver.dto.mail.MailDTO;
import me.jjh.mapia.webserver.dto.mail.MailTplDTO;
import org.springframework.stereotype.Service;

/**
 * packageName    : me.jjh.mapia.service.mail
 * fileName       : MailService
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

public interface MailService {

    /**
     * 메일 템플릿 조회
     * @param mailTplKey
     * @return
     */
    MailTplDTO findMailTplByKey(String mailTplKey);

    /**
     * 메일 발송
     * @param mailDTO
     * @param emailTplCode
     */
    void sendEmail(MailDTO mailDTO, EmailTplCode emailTplCode);

    /**
     * 인증 메일 생성
     * @param memberId
     * @param emailTplCode
     */
    void sendVerifyEmailAsync(String memberId, String verifyToken, String verifyCode, EmailTplCode emailTplCode);

    /**
     * 메일 발송
     * @param mailDTO
     */
    void executeSendMail(MailDTO mailDTO);

}
