package me.jjh.mapia.webserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.*;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import me.jjh.mapia.webserver.common.response.ApiResponse;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import me.jjh.mapia.webserver.request.member.SignupReqDTO;
import me.jjh.mapia.webserver.request.member.SignupVerifyReqDTO;
import me.jjh.mapia.webserver.service.member.LocalMemberService;
import me.jjh.mapia.webserver.service.member.Oauth2MemberService;
import me.jjh.mapia.webserver.service.redis.RedisService;
import me.jjh.mapia.webserver.service.verify.VerifyService;
import me.jjh.mapia.webserver.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : me.jjh.mapia.controller
 * fileName       : BoardController
 * author         : JJH
 * date           : 2025-01-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-11        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class LoginController {

    private final LocalMemberService localMemberService;
    private final VerifyService verifyService;
    private final RedisService redisService;
    private final UserSessionUtil userSessionUtil;
    private final Oauth2MemberService oauth2MemberService;
    private final ObjectMapper redisOjectMapper;

    /**
     * 자체 로그인 폼
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "session", required = false) String session, @RequestParam(value = "memberId", required = false) String memberId,
                        Model model, HttpServletRequest request) {
        log.debug("[LOGIN CONTROLLER - login] START");

        if ("expired".equals(session)) {
            return ReturnUtil.move(model, new AlertResponse(ComCode.LOGIN_SESSION_EXPIRED, AlertType.ERROR, "/auth/login"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            log.debug("[LOGIN CONTROLLER - login] 기존 세션 활성화 확인 : 인증된 사용자 [{}] 메인 페이지로 이동합니다.", authentication.getName());
            log.debug("[LOGIN CONTROLLER - login] END");
            return "redirect:/"; // 메인 페이지로 리다이렉트
        }

        // 현재 활성화된 모든 사용자 목록 로깅
        List<String> activeUsers = userSessionUtil.getActiveUsers();
        log.debug("[LOGIN CONTROLLER - login] 현재 활성 사용자 수: {}", activeUsers.size());
        if (!activeUsers.isEmpty()) {
            log.debug("[LOGIN CONTROLLER - login] 활성 사용자 목록: {}", String.join(", ", activeUsers));
        } else {
            log.debug("[LOGIN CONTROLLER - login] 현재 활성 사용자가 없습니다.");
        }

        model.addAttribute("memberId", memberId);

        log.debug("[LOGIN CONTROLLER - login] END");

        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(@RequestParam(value = "memberId", required = false) String memberId, Model model) {
        log.debug("[LOGIN CONTROLLER - signup] START");

        model.addAttribute("enumEmail", EnumUtil.getEnumList(EmailCode.class));
        model.addAttribute("memberId", SecurityUtil.paramDecrypt(memberId));

        log.debug("[LOGIN CONTROLLER - signup] END");

        return "auth/signup";
    }

    @PostMapping("/signupProc")
    public ResponseEntity<ApiResponse<Object>> signupProc(@Valid @RequestBody SignupReqDTO signupReqDTO,
                                                          BindingResult bindingResult) {
        log.debug("[LOGIN CONTROLLER - signupProc] START");

        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            log.warn("[LOGIN CONTROLLER - signupProc] 회원가입 입력 검증에 실패했습니다 field[{}]", error);
            return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(ApiResponse.error(ErrorCode.VALIDATION_ERROR));
        }

        VerifyDTO verifyDTO = localMemberService.signupVerifySend(signupReqDTO.getMemberId(), signupReqDTO.getMemberPwd());

        HashMap<String, Object> data = new HashMap<>();
        data.put("verifyToken", verifyDTO.getVerifyToken());

        log.debug("[LOGIN CONTROLLER - signupProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.SIGNUP_VERIFY_SEND_SUCCESS, data));
    }

    @GetMapping("/signupVerify")
    public String signupVerify(@RequestParam(value = "verifyToken", required = false) String token, Model model) {
        log.debug("[LOGIN CONTROLLER - signupVerify] START");

        if (DataVaildUtil.isStringEmpty(token)) {
            log.warn("[LOGIN CONTROLLER - signupVerify] 요청된 토큰이 없습니다");
            return ReturnUtil.move(model, new AlertResponse(ComCode.SIGNUP_INVALID_TOKEN, AlertType.ERROR, "/auth/signup"));
        }

        VerifyDTO verifyDTO = verifyService.findVerifyByToken(token);

        /* 토큰 검증 */
        if(DataVaildUtil.isObjectEmpty(verifyDTO)) {
            log.warn("[LOGIN CONTROLLER - signupVerify] 유효하지 않은 토큰입니다 verifyToken[{}]", token);
            return ReturnUtil.move(model, new AlertResponse(ComCode.SIGNUP_INVALID_TOKEN, AlertType.ERROR, "/auth/signup"));
        }

        /* 인증 완료 여부 */
        MemberDTO memberDTO = localMemberService.findMemberById(verifyDTO.getVerifyTarget());

        if (DataVaildUtil.isObjectEmpty(memberDTO)) {
            log.warn("[LOGIN CONTROLLER - signupVerify] 회원가입 신청이 되지 않은 아이디입니다 memberId[{}]", verifyDTO.getVerifyTarget());
            return ReturnUtil.move(model, new AlertResponse(ComCode.SIGNUP_INVALID_USER, AlertType.ERROR, "/auth/signup"));
        }

        if("Y".equals(memberDTO.getIsActive())) {
            log.warn("[LOGIN CONTROLLER - signupVerify] 이미 인증된 사용자입니다 memberId[{}]", memberDTO.getMemberId());
            return ReturnUtil.move(model, new AlertResponse(ComCode.SIGNUP_VERIFY_ALREADY_MEMBER, AlertType.WARNING, "/auth/login"));
        }

        /* redis 조회 */
        Object getRedis = redisService.get(RedisKeyCode.SIGNUP_EMAIL.getType(), token);

        HashMap<String, Object> data = new HashMap<>();
        data.put("verifyToken", token);

        if (DataVaildUtil.isObjectEmpty(getRedis)) {
            log.warn("[LOGIN CONTROLLER - signupVerify] Redis에서 토큰을 찾을 수 없습니다 verifyToken[{}]", token);
            return ReturnUtil.confirm(model, new AlertResponse(ComCode.SIGNUP_EXPIRED_TOKEN, AlertType.WARNING,
                    AlertType.AJAX, "/auth/signupVerifyResend",
                    MapUtil.toJson(data),
                    AlertType.GET, "/auth/login"
                    ));
        }

        Map<String, Object> redisMap = redisOjectMapper.convertValue(getRedis, new TypeReference<Map<String, Object>>() {});

        /* 토큰 만료 검증 */
        String rdsVerifyExpAt = (String) redisMap.get("verifyExpAt");
        if(DataVaildUtil.isStringEmpty(rdsVerifyExpAt) || DateUtil.isExpired(LocalDateTime.parse(rdsVerifyExpAt))) {
            log.warn("[LOGIN CONTROLLER - signupVerify] 토큰이 만료되었습니다 verifyToken[{}]", token);
            return ReturnUtil.confirm(model, new AlertResponse(ComCode.SIGNUP_EXPIRED_TOKEN, AlertType.WARNING,
                    AlertType.AJAX, "/auth/signupVerifyResend",
                    MapUtil.toJson(data),
                    AlertType.GET, "/auth/login"
                    ));
        }

        // 만료 시간(밀리초)만 JSP로 전달
        Long verifyExpAt = DateUtil.convertDate(verifyDTO.getVerifyExpAt(), Long.class, false);

        model.addAttribute("verifyToken", verifyDTO.getVerifyToken());
        model.addAttribute("verifyExpAt", verifyExpAt);

        log.debug("[LOGIN CONTROLLER - signupVerify] END");

        return "auth/signupVerify";
    }

    @PostMapping("/signupVerifyResend")
    public ResponseEntity<ApiResponse<Object>> signupVerifyResend(@Valid @RequestBody SignupVerifyReqDTO signupVerifyReqDTO,
                                                                  BindingResult bindingResult) {

        log.debug("[LOGIN CONTROLLER - signupVerifyResend] START");

        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            log.warn("[LOGIN CONTROLLER - signupVerifyResend] 회원가입 인증코드 재발송 검증에 실패했습니다 field[{}]", error);
            return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(ApiResponse.error(ErrorCode.VALIDATION_ERROR));
        }

        if (DataVaildUtil.isStringEmpty(signupVerifyReqDTO.getVerifyToken())) {
            log.warn("[LOGIN CONTROLLER - signupVerifyResend] 요청된 토큰이 없습니다");
            return ResponseEntity.status(ComCode.SIGNUP_INVALID_TOKEN.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_INVALID_TOKEN));
        }

        VerifyDTO verifyDTO = localMemberService.signupVerifyReSend(signupVerifyReqDTO.getVerifyToken());

        HashMap<String, Object> data = new HashMap<>();
        data.put("verifyToken", verifyDTO.getVerifyToken());
        data.put("verifyCode", verifyDTO.getVerifyCode());

        log.debug("[LOGIN CONTROLLER - signupVerifyResend] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.SIGNUP_VERIFY_SEND_SUCCESS, data));
    }

    @PostMapping("/signupVerifyProc")
    public ResponseEntity<ApiResponse<Object>> signupVerifyProc(@Valid @RequestBody SignupVerifyReqDTO signupVerifyReqDTO,
                                                                BindingResult bindingResult) {
        log.debug("[LOGIN CONTROLLER - signupVerifyProc] START");

        String token = signupVerifyReqDTO.getVerifyToken();
        String verifyCode = signupVerifyReqDTO.getVerifyCode();

        if (DataVaildUtil.isStringEmpty(token)) {
            log.warn("[LOGIN CONTROLLER - signupVerifyProc] 요청된 토큰이 없습니다");
            return ResponseEntity.status(ComCode.SIGNUP_INVALID_TOKEN.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_INVALID_TOKEN));
        }

        if (DataVaildUtil.isStringEmpty(verifyCode)) {
            log.warn("[LOGIN CONTROLLER - signupVerifyProc] 인증코드가 유효하지 않습니다 verifyCode[{}]", verifyCode);
            return ResponseEntity.status(ComCode.SIGNUP_INVALID_CODE.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_INVALID_CODE));
        }

        /* redis 조회 */
        Object getRedis = redisService.get(RedisKeyCode.SIGNUP_EMAIL.getType(), token);

        if (DataVaildUtil.isObjectEmpty(getRedis)) {
            log.warn("[LOGIN CONTROLLER - signupVerifyProc] Redis에서 토큰을 찾을 수 없습니다. verifyToken[{}]", token);
            return ResponseEntity.status(ComCode.SIGNUP_EXPIRED_TOKEN.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_EXPIRED_TOKEN));
        }

        Map<String, Object> redisMap = redisOjectMapper.convertValue(getRedis, new TypeReference<Map<String, Object>>() {});

        /* 토큰 검증 */
        String memberId = (String) redisMap.get("memberId");
        String rdsVerifyCode = (String) redisMap.get("verifyCode");
        String rdsisVerifyExpAtStr = (String) redisMap.get("verifyExpAt");

        if(DataVaildUtil.isStringEmpty(rdsisVerifyExpAtStr) || DateUtil.isExpired(LocalDateTime.parse(rdsisVerifyExpAtStr))) {
            log.warn("[LOGIN CONTROLLER - signupVerifyProc] 토큰이 만료되었습니다. verifyToken[{}]", token);
            return ResponseEntity.status(ComCode.SIGNUP_EXPIRED_TOKEN.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_EXPIRED_TOKEN));
        }

        if(DataVaildUtil.isStringEmpty(rdsVerifyCode) || !rdsVerifyCode.equals(verifyCode)) {
            log.warn("[LOGIN CONTROLLER - signupVerifyProc] 인증코드가 유효하지 않습니다 verifyCode[{}]", verifyCode);
            return ResponseEntity.status(ComCode.SIGNUP_INVALID_CODE.getStatus()).body(ApiResponse.error(ComCode.SIGNUP_INVALID_CODE));
        }

        // 기존 인증 정보 REDIS 삭제
        redisService.delete(RedisKeyCode.SIGNUP_EMAIL.getType(), token);

        //회원 상태 업데이트
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberId);
        memberDTO.setIsActive("Y");
        memberDTO.setUpdatedIdx(0L);
        memberDTO.setUpdatedId("SYSTEM");
        localMemberService.updateMember(memberDTO);

        log.debug("[LOGIN CONTROLLER - signupVerifyProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.SIGNUP_SUCCESS));
    }

    @PostMapping("/oauth2SignupProc")
    public ResponseEntity<ApiResponse<Object>> oauth2SignupProc(@RequestBody Map<String, String> requestBody) {
        log.debug("[LOGIN CONTROLLER - oauth2SignupProc] START");

        String oauth2Email = SecurityUtil.paramDecrypt(requestBody.get("oauth2Email"));
        String oauth2Provider = SecurityUtil.paramDecrypt(requestBody.get("oauth2Provider"));
        String oauth2ProviderId = SecurityUtil.paramDecrypt(requestBody.get("oauth2ProviderId"));
        oauth2MemberService.createOauth2Member(oauth2Email, oauth2Provider, oauth2ProviderId);

        log.debug("[LOGIN CONTROLLER - oauth2SignupProc] END");

        return ResponseEntity.ok(ApiResponse.success(ComCode.OAUTH2_SIGNUP_SUCCESS, null, "/auth/login"));
    }
}
