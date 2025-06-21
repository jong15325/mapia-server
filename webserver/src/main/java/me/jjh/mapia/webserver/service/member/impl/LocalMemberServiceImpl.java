package me.jjh.mapia.webserver.service.member.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.code.EmailTplCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.common.code.RedisKeyCode;
import me.jjh.mapia.webserver.common.exception.BusinessException;
import me.jjh.mapia.webserver.common.exception.ServerException;
import me.jjh.mapia.webserver.dto.member.MemberAuthDTO;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.member.RoleDTO;
import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import me.jjh.mapia.webserver.mapper.MemberMapper;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.service.mail.MailService;
import me.jjh.mapia.webserver.service.member.LocalMemberService;
import me.jjh.mapia.webserver.service.member.MemberAuthService;
import me.jjh.mapia.webserver.service.redis.RedisService;
import me.jjh.mapia.webserver.service.role.RoleService;
import me.jjh.mapia.webserver.service.verify.VerifyService;
import me.jjh.mapia.webserver.util.DataVaildUtil;
import me.jjh.mapia.webserver.util.DateUtil;
import me.jjh.mapia.webserver.util.SecurityUtil;
import me.jjh.mapia.webserver.util.UserAgentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *packageName    : me.jjh.mapia.service.member.local.impl
 * fileName       : LocalMemberServiceImpl
 * author         : JJH
 * date           : 2025-02-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-21        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalMemberServiceImpl implements LocalMemberService {

    private final MemberMapper memberMapper;

    private final RedisService redisService;

    private final MailService mailService;

    private final VerifyService verifyService;

    private final RoleService roleService;

    private final MemberAuthService memberAuthService;

    @Override
    public MemberDTO findMemberByIdx(Long memberIdx) {

        log.debug("[LOCAL MEMBER SERVICE - findMemberByIdx] START");

        log.debug("[LOCAL MEMBER SERVICE - findMemberByIdx] END");

        return memberMapper.findMemberByIdx(memberIdx);
    }

    @Override
    public MemberDTO findMemberById(String memberId) {

        log.debug("[LOCAL MEMBER SERVICE - findMemberById] START");

        log.debug("[LOCAL MEMBER SERVICE - findMemberById] END");

        return memberMapper.findMemberById(memberId);
    }

    @Override
    public MemberDTO findMemberWithAuthById(String memberId) {
        log.debug("[LOCAL MEMBER SERVICE - findMemberWithAuthById] START");

        MemberDTO memberDTO = findMemberById(memberId);

        if (!DataVaildUtil.isObjectEmpty(memberDTO)) {
            List<MemberAuthDTO> authList = memberAuthService.findMemberAuthByMemberIdx(memberDTO.getMemberIdx());
            memberDTO.setMemberAuths(authList);
        }

        log.debug("[LOCAL MEMBER SERVICE - findMemberWithAuthById] END");

        return memberDTO;
    }

    @Override
    public void insertMember(MemberDTO memberDTO) {
        log.debug("[LOCAL MEMBER SERVICE - insertMember] START");

        memberMapper.insertMember(memberDTO);

        log.debug("[LOCAL MEMBER SERVICE - insertMember] END");
    }

    @Override
    public void updateMember(MemberDTO memberDTO) {
        log.debug("[LOCAL MEMBER SERVICE - updateMember] START");

        memberMapper.updateMember(memberDTO);

        log.debug("[LOCAL MEMBER SERVICE - updateMember] END");
    }

    @Transactional
    @Override
    public VerifyDTO signupVerifySend(String memberId, String memberPwd) {
        log.debug("[LOCAL MEMBER SERVICE - signupVerifySend] START");

        // 중복 회원 검사
        if (memberMapper.countMemberById(memberId) > 0) {
            log.warn("[LOCAL MEMBER SERVICE - signupVerifySend] 중복된 사용자가 존재합니다 memberId[{}]", memberId);
            throw new BusinessException(ErrorCode.DUPLICATE_MEMBER);
        }

        /* 인증 정보 생성 */
        String verifyCode = SecurityUtil.generateNumberCode(4); // 인증 코드
        LocalDateTime verifyIssuedAt = LocalDateTime.now(); // 발급 시간
        LocalDateTime verifyExpAt = DateUtil.adjustDateTime(RedisKeyCode.SIGNUP_EMAIL.getDuration()); // 만료 시간
        String verifyToken = SecurityUtil.generateVerifyToken(
                DateUtil.convertDate(verifyIssuedAt, Date.class, false),
                DateUtil.convertDate(verifyExpAt, Date.class, false),
                RedisKeyCode.SIGNUP_EMAIL
        );

        // 멤버 등록
        RoleDTO roleDTO = roleService.findDefaultRole();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberId);
        memberDTO.setMemberLoginIp(UserAgentUtil.getClientIp(null));
        memberDTO.setMemberRoleIdx(roleDTO.getRoleIdx());
        memberDTO.setCreatedIdx(0L);
        memberDTO.setCreatedId("SYSTEM");
        insertMember(memberDTO);
        if(!DataVaildUtil.isNumberNull(memberDTO.getMemberIdx())) {
            MemberAuthDTO memberAuthDTO = new MemberAuthDTO();
            memberAuthDTO.setMemberIdx(memberDTO.getMemberIdx());
            memberAuthDTO.setMemberAuthPassword(SecurityUtil.encryptPassword(memberPwd));
            memberAuthService.insertMemberAuth(memberAuthDTO);
        } else {
            throw new ServerException(ErrorCode.DB_SAVE_FAILED);
        }

        // 인증 정보 REDIS 저장
        VerifyDTO verifyDTO = new VerifyDTO();
        verifyDTO.setVerifyType(RedisKeyCode.SIGNUP_EMAIL.getType());
        verifyDTO.setVerifyTarget(memberDTO.getMemberId());
        verifyDTO.setVerifyToken(verifyToken);
        verifyDTO.setVerifyIssuedAt(verifyIssuedAt);
        verifyDTO.setVerifyExpAt(verifyExpAt);
        verifyDTO.setVerifyCnt(0);
        verifyDTO.setVerifyIp(UserAgentUtil.getClientIp(null));
        verifyDTO.setVerifyAgent(UserAgentUtil.getUserAgentInfo(null));
        verifyDTO.setCreatedIdx(0L);
        verifyDTO.setCreatedId("SYSTEM");

        HashMap<String, Object> redisMap = new HashMap<>();
        redisMap.put("memberId", memberDTO.getMemberId());
        redisMap.put("verifyToken", verifyToken);
        redisMap.put("verifyCode", verifyCode);
        redisMap.put("verifyIssuedAt", verifyIssuedAt);
        redisMap.put("verifyExpAt", verifyExpAt);

        // 인증 정보 REDIS 저장(패스워드 포함) - 비영구
        redisService.set(RedisKeyCode.SIGNUP_EMAIL.getType(), verifyToken, redisMap, RedisKeyCode.SIGNUP_EMAIL.getDuration());

        // 인증 정보 DB 저장(패스워드 제외) - 영구
        verifyService.insertVerify(verifyDTO);

        /* 인증 메일 전송 */
        mailService.sendVerifyEmailAsync("howeer15325@naver.com", verifyToken, verifyCode, EmailTplCode.SIGNUP_VERIFY);

        log.debug("[LOCAL MEMBER SERVICE - signupVerifySend] END");

        return verifyDTO;
    }

    @Override
    @Transactional
    public VerifyDTO signupVerifyReSend(String token) {

        log.debug("[LOCAL MEMBER SERVICE - signupVerifyReSend] START");

        if (token == null || token.isEmpty()) {
            log.warn("[LOCAL MEMBER SERVICE - signupVerifyReSend] 토큰이 없습니다.");
            throw new BusinessException(ComCode.SIGNUP_INVALID_TOKEN);
        }

        VerifyDTO verifyDTO = verifyService.findVerifyByToken(token);

        if(verifyDTO == null) {
            log.warn("[LOGIN CONTROLLER - signupVerifyResend] 유효하지 않은 토큰입니다 tk[{}]", token);
            throw new BusinessException(ComCode.SIGNUP_INVALID_TOKEN);
        }

        /* 인증 완료 여부 */
        MemberDTO memberDTO = findMemberById(verifyDTO.getVerifyTarget());

        if (memberDTO == null) {
            log.warn("[LOGIN CONTROLLER - signupVerifyResend] 회원가입 신청이 되지 않은 아이디입니다 memberId[{}]", verifyDTO.getVerifyTarget());
            throw new BusinessException(ComCode.SIGNUP_INVALID_USER);
        }

        if("Y".equals(memberDTO.getIsActive())) {
            log.warn("[LOGIN CONTROLLER - signupVerifyResend] 이미 인증된 사용자입니다 memberId[{}]", memberDTO.getMemberId());
            throw new BusinessException(ComCode.SIGNUP_VERIFY_ALREADY_MEMBER);
        }

        String memberId = memberDTO.getMemberId();

        // 인증 정보 생성
        String newVerifyCode = SecurityUtil.generateNumberCode(4);
        LocalDateTime newVerifyIssuedAt = LocalDateTime.now();
        LocalDateTime newVerifyExpAt = DateUtil.adjustDateTime(RedisKeyCode.SIGNUP_EMAIL.getDuration());
        String newVerifyToken = SecurityUtil.generateVerifyToken(
                DateUtil.convertDate(newVerifyIssuedAt, Date.class, false),
                DateUtil.convertDate(newVerifyExpAt, Date.class, false),
                RedisKeyCode.SIGNUP_EMAIL
        );

        // 기존 인증 정보 REDIS 삭제
        redisService.delete(RedisKeyCode.SIGNUP_EMAIL.getType(), token);

        // 인증 정보 REDIS 저장
        VerifyDTO newVerifyDTO = new VerifyDTO();
        newVerifyDTO.setVerifyType(RedisKeyCode.SIGNUP_EMAIL.getType());
        newVerifyDTO.setVerifyTarget(memberId);
        newVerifyDTO.setVerifyToken(newVerifyToken);
        newVerifyDTO.setVerifyIssuedAt(newVerifyIssuedAt);
        newVerifyDTO.setVerifyExpAt(newVerifyExpAt);
        newVerifyDTO.setVerifyIp(UserAgentUtil.getClientIp(null));
        newVerifyDTO.setVerifyAgent(UserAgentUtil.getUserAgentInfo(null));
        newVerifyDTO.setCreatedIdx(0L);
        newVerifyDTO.setCreatedId("SYSTEM");

        // Redis에 새 정보 저장
        HashMap<String, Object> redisMap = new HashMap<>();
        redisMap.put("memberId", memberId);
        redisMap.put("verifyToken", newVerifyToken);
        redisMap.put("verifyCode", newVerifyCode);
        redisMap.put("verifyIssuedAt", newVerifyIssuedAt);
        redisMap.put("verifyExpAt", newVerifyExpAt);

        // 기존 인증 정보 비활성화
        verifyDTO.setIsActive("N");
        verifyService.updateVerifyActive(verifyDTO);

        // 인증 정보 REDIS 저장(패스워드 포함) - 비영구
        redisService.set(RedisKeyCode.SIGNUP_EMAIL.getType(), newVerifyToken, redisMap, RedisKeyCode.SIGNUP_EMAIL.getDuration());

        // 인증 정보 DB 저장(패스워드 제외) - 영구
        verifyService.insertVerify(newVerifyDTO);

        // 인증 메일 전송
        mailService.sendVerifyEmailAsync("howeer15325@naver.com", newVerifyToken, newVerifyCode, EmailTplCode.SIGNUP_VERIFY);

        log.debug("[LOCAL MEMBER SERVICE - signupVerifyReSend] END");

        return newVerifyDTO;
    }
}
