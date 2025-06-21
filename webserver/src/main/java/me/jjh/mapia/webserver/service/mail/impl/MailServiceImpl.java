package me.jjh.mapia.webserver.service.mail.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.*;
import me.jjh.mapia.webserver.common.exception.BusinessException;
import me.jjh.mapia.webserver.common.exception.ServerException;
import me.jjh.mapia.webserver.dto.mail.MailDTO;
import me.jjh.mapia.webserver.dto.mail.MailTplDTO;
import me.jjh.mapia.webserver.mapper.MailMapper;
import me.jjh.mapia.webserver.properties.MailProperties;
import me.jjh.mapia.webserver.properties.ServerProperties;
import me.jjh.mapia.webserver.service.mail.MailService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import me.jjh.mapia.webserver.util.DateUtil;
import me.jjh.mapia.webserver.util.EmailTplUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * packageName    : me.jjh.mapia.service.mail.impl
 * fileName       : MailServiceImpl
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    private final ServerProperties serverProperties;

    private final MailMapper mailMapper;

    @Override
    public MailTplDTO findMailTplByKey(String mailTplKey) {
        return mailMapper.findMailTplByKey(mailTplKey);
    }

    private void insertMail(MailDTO mailDTO) {
        mailMapper.insertMail(mailDTO);
    }

    @Override
    public void executeSendMail(MailDTO mailDTO) {
        log.debug("[MAIL SERVICE - executeSendMail] START");

        mailDTO.setMailStatus(StatusCode.PENDING.getValue());

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailDTO.getMailSndr());
            helper.setTo(mailDTO.getMailRecv());
            helper.setSubject(mailDTO.getMailTitle());
            helper.setText(mailDTO.getMailContent(), true);

            log.debug("title: {} ", mailDTO.getMailTitle());
            log.debug("content: {} ", mailDTO.getMailContent());

            // TO와 CC 정보를 헤더에 추가하여 수신자에게 표시
            if (!DataVaildUtil.isListEmpty(mailDTO.getMailRecvList())) {
                message.setHeader("To", String.join(", ", mailDTO.getMailRecvList()));
            }

            if (!DataVaildUtil.isListEmpty(mailDTO.getMailCcList())) {
                message.setHeader("Cc", String.join(", ", mailDTO.getMailCcList()));
            }

            mailSender.send(message);

            mailDTO.setMailStatus(StatusCode.SUCCESS.getValue());

        } catch (MessagingException e) {
            log.info("[MAIL SERVICE - executeSendMail] MessagingException 메일 전송 중 문제가 발생했습니다 EMAIL[{}]", mailDTO.getMailRecv());
            mailDTO.setMailStatus(StatusCode.FAILED.getValue());
            throw new ServerException(ErrorCode.MAIL_SEND_FAILED);
        } catch (ServerException e) {
            log.info("[MAIL SERVICE - executeSendMail] ServerException 메일 전송 중 문제가 발생했습니다 EMAIL[{}]", mailDTO.getMailRecv());
            mailDTO.setMailStatus(StatusCode.FAILED.getValue());
            throw new ServerException(ErrorCode.MAIL_SEND_FAILED);
        } finally {
            insertMail(mailDTO);
            log.debug("[MAIL SERVICE - executeSendMail] END");
        }
    }

    @Override
    public void sendEmail(MailDTO mailDTO, EmailTplCode emailTplCode) {
        log.debug("[MAIL SERVICE - sendEmail] START");

        // 1. 이메일 템플릿 KEY 확인
        if (emailTplCode == null || DataVaildUtil.isStringEmpty(emailTplCode.getMailTplKey())) {
            log.warn("[MAIL SERVICE - sendEmail] BusinessException 템플릿 키가 존재하지 않습니다");
            throw new BusinessException(ErrorCode.NOT_MAIL_KEY);
        }

        // 2. 이메일 템플릿 조회
        MailTplDTO mailTplDTO = mailMapper.findMailTplByKey(emailTplCode.getMailTplKey());
        if(mailTplDTO == null || DataVaildUtil.isObjectEmpty(mailTplDTO)) {
            log.warn("[MAIL SERVICE - sendEmail] BusinessException 템플릿이 존재하지 않습니다 Key[{}]", emailTplCode.getMailTplKey());
            throw new BusinessException(ErrorCode.NOT_MAIL_TPL);
        }

        // 3. 수신자 확인
        if (mailDTO == null || DataVaildUtil.isListEmpty(mailDTO.getMailRecvList())) {
            log.warn("[MAIL SERVICE - sendEmail] BusinessException 수신자가 존재하지 않습니다 Recv[{}]", mailDTO.getMailRecvList());
            throw new BusinessException(ErrorCode.NOT_MAIL_RECV);
        }

        // 5. 치환할 변수 데이터 설정
        Map<String, String> variables = new HashMap<>();
        variables.put("title", mailDTO.getMailTitle());
        variables.put("content", mailDTO.getMailContent());
        variables.put("year", String.valueOf(LocalDateTime.now().getYear()));

        // 6. 이메일 템플릿 변환 (치환된 데이터 적용)
        String reaplceContent = EmailTplUtil.replaceVars(mailTplDTO.getMailTplContent(), variables, emailTplCode);
        mailTplDTO.setMailTplContent(reaplceContent);
        mailDTO.setMailTpl(mailTplDTO);

        // 7. 발송 로직 시작
        executeSendMail(mailDTO);

        log.debug("[MAIL SERVICE - sendEmail] END");
    }

    @Async
    @Override
    public void sendVerifyEmailAsync(String memberId, String verifyToken, String verifyCode, EmailTplCode emailTplCode) {
        log.debug("[MAIL SERVICE - sendVerifyEmail] START");

        // 1. 이메일 템플릿 조회
        MailTplDTO mailTplDTO = mailMapper.findMailTplByKey(emailTplCode.getMailTplKey());
        if(mailTplDTO == null || DataVaildUtil.isObjectEmpty(mailTplDTO)) {
            log.warn("[MAIL SERVICE - sendVerifyEmailAsync] BusinessException 템플릿이 존재하지 않습니다 Key[{}]", emailTplCode.getMailTplKey());
            throw new BusinessException(ErrorCode.NOT_MAIL_TPL);
        }

        // 2. 수신자 확인
        if (DataVaildUtil.isStringEmpty(memberId)) {
            log.warn("[MAIL SERVICE - sendVerifyEmailAsync] BusinessException 수신자가 존재하지 않습니다 Recv[{}]", memberId);
            throw new BusinessException(ErrorCode.NOT_MAIL_RECV);
        }

        // 3. 인증 데이터 생성
        LocalDateTime verificationExpAt = DateUtil.adjustDateTime(RedisKeyCode.SIGNUP_EMAIL.getDuration()); // 만료 시간

        // 4. 치환할 변수 데이터 설정
        Map<String, String> variables = new HashMap<>();
        variables.put("memberId", memberId);
        variables.put("verifyCode", verifyCode);
        variables.put("mailExpTime", DateUtil.formatDateTime(verificationExpAt, DateFormatCode.STANDARD_KR));
        variables.put("directUrl", serverProperties.getServerAddress() + "/auth/signupVerify?verifyToken=" + verifyToken);
        variables.put("year", String.valueOf(LocalDateTime.now().getYear()));

        // 5. mail 데이터 담기
        MailDTO mailDTO = new MailDTO();
        mailDTO.setMailSndr(mailProperties.getUsername());
        mailDTO.setMailRecvType("TO");
        mailDTO.setMailRecv(memberId);
        mailDTO.setMailTitle(mailTplDTO.getMailTplTitle());
        mailDTO.setMailContent(EmailTplUtil.replaceVars(mailTplDTO.getMailTplContent(), variables, emailTplCode));

        // 6. 발송 로직 시작
        executeSendMail(mailDTO);

        log.debug("[MAIL SERVICE - sendVerifyEmail] END");
    }

}
