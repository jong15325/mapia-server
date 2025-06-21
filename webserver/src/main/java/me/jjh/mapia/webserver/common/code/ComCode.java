package me.jjh.mapia.webserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * packageName    : me.jjh.mapia.common.code
 * fileName       : ComCode
 * author         : JJH
 * date           : 2025-02-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-22        JJH       최초 생성
 */

@Getter
@RequiredArgsConstructor
public enum ComCode implements Code {

    // CRUD
    INSERT_SUCCESS(HttpStatus.OK,"CRUD_001", "등록을 완료했습니다", ""),
    UPDATE_SUCCESS(HttpStatus.OK,"CRUD_002", "수정을 완료했습니다", ""),
    DELETE_SUCCESS(HttpStatus.OK,"CRUD_003", "삭제를 완료했습니다", ""),
    READ_SUCCESS(HttpStatus.BAD_REQUEST,"CRUD_004", "조회를 성공했습니다", ""),
    INSERT_FAIL(HttpStatus.BAD_REQUEST,"CRUD_005", "등록을 실패했습니다", ""),
    UPDATE_FAIL(HttpStatus.BAD_REQUEST,"CRUD_006", "수정을 실패했습니다", ""),
    DELETE_FAIL(HttpStatus.BAD_REQUEST,"CRUD_007", "삭제를 실패했습니다", ""),
    READ_FAIL(HttpStatus.BAD_REQUEST,"CRUD_008", "조회를 실패했습니다", ""),

    // LOGIN
    LOGIN_SUCCESS(HttpStatus.OK,"LOGIN_001", "로그인에 성공했습니다", ""),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "LOGIN_002", "로그인에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    LOGIN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "LOGIN_003", "계정 정보가 일치하지 않습니다", "관리자에게 문의해주세요"),
    LOGIN_LOCKED(HttpStatus.FORBIDDEN, "LOGIN_004", "잠긴 계정입니다", "관리자에게 문의해주세요"),
    LOGIN_SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "LOGIN_005", "세션이 만료되었습니다", "다시 로그인해주세요"),
    LOGIN_DISABLED(HttpStatus.FORBIDDEN, "LOGIN_006", "인증이 필요하거나 비활성화된 계정입니다", "관리자에게 문의해주세요"),
    LOGIN_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LOGIN_999", "로그인 중 문제가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요"),

    // OAUTH
    OAUTH2_SIGNUP_CONFIRM(HttpStatus.OK, "OAUTH2_001", "소셜 이메일로 등록된 회원 정보가 없습니다", "확인 버튼 클릭 시 회원가입이 자동으로 진행됩니다"),
    OAUTH2_SIGNUP_SUCCESS(HttpStatus.OK, "OAUTH2_002", "회원가입이 완료되었습니다", "로그인을 다시 진행해주세요"),
    OAUTH2_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "OAUTH2_003", "로그인 중 문제가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    OAUTH2_LINK_REQUIRE(HttpStatus.OK, "OAUTH2_004", "소셜 이메일로 등록된 회원 정보가 있습니다", "해당 이메일로 가입한 계정이 기억나지 않으시면 아이디/비밀번호 찾기를 이용한 후 로그인하여 설정에서 소셜 로그인 연동이 가능합니다"),

    // SIGNUP
    SIGNUP_SUCCESS(HttpStatus.OK,"SIGNUP_001", "인증이 완료되었습니다", "메인 페이지로 이동합니다, 로그인을 다시 해주세요"),
    SIGNUP_VERIFY_SEND_SUCCESS(HttpStatus.OK,"SIGNUP_002", "인증 메일을 발송했습니다", "메일을 확인해주세요"),
    SIGNUP_DUPLICATE_ID(HttpStatus.BAD_REQUEST, "SIGNUP_003", "이미 존재하는 이메일입니다", "다른 이메일을 사용해주세요"),
    SIGNUP_INVALID_EMAIL(HttpStatus.BAD_REQUEST, "SIGNUP_004", "유효하지 않은 이메일 형식입니다", "ex) ABC@example.com"),
    SIGNUP_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "SIGNUP_005", "계정 정보가 일치하지 않습니다", ""),
    SIGNUP_EMAIL_VERIFICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "SIGNUP_006", "이메일 인증이 필요합니다", "회원가입 시 입력한 이메일을 확인해주세요"),
    SIGNUP_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "SIGNUP_006", "유효하지 않은 토큰입니다", "회원가입을 다시 시도해주세요"),
    SIGNUP_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "SIGNUP_007", "만료된 토큰입니다", "인증 코드를 재발급하시겠습니까?"),
    SIGNUP_VERIFY_ALREADY_MEMBER(HttpStatus.BAD_REQUEST, "SIGNUP_008", "이미 인증된 사용자힙니다", "로그인 화면으로 돌아갑니다"),
    SIGNUP_INVALID_USER(HttpStatus.BAD_REQUEST, "SIGNUP_009", "회원가입 신청이 되지 않은 아이디입니다", "회원가입을 다시 시도해주세요"),
    SIGNUP_INVALID_CODE(HttpStatus.BAD_REQUEST, "SIGNUP_010", "유효하지 않은 인증코드입니다", "인증을 다시 시도해주세요"),

    // LOGOUT
    LOGOUT_SUCCESS(HttpStatus.OK,"LOGOUT_001", "로그아웃 되었습니다", ""),
    LOGOUT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"LOGOUT_002", "로그아웃 중 문제가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요"),

    //BOARD

    //FILE_UPLOAD
    FILE_UPLOAD_SUCCES(HttpStatus.OK,"FILE_UPLOAD_001", "파일 업로드에 성공했습니다", ""),
    FILE_UPLOAD_EMPTY(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_002", "파일이 비어있어 파일 업로드에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    FILE_UPLOAD_MAX_SIZE(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_003", "파일의 허용용량을 초과하여 파일 업로드에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    FILE_UPLOAD_MIN_SIZE(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_004", "파일의 용량이 너무 작아 파일 업로드에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    FILE_UPLOAD_EXT_ALLOWED(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_005", "허용되지 않는 파일 형식입니다", "문제가 지속되면 관리자에게 문의해주세요"),
    FILE_UPLOAD_NOT_EXT(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_006", "파일에 확장자가 없거나 파일명이 유효하지 않습니다", "문제가 지속되면 관리자에게 문의해주세요"),
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST,"FILE_UPLOAD_007", "파일 업로드에 실패했습니다", "문제가 지속되면 관리자에게 문의해주세요"),

    FILE_DOWNLOAD_SUCCESS(HttpStatus.OK, "FILE_DOWNLOAD_001", "파일 다운로드에 성공했습니다", ""),
    FILE_DOWNLOAD_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_DOWNLOAD_002", "요청한 파일을 찾을 수 없습니다", "파일이 삭제되었거나 이동되었을 수 있습니다"),
    FILE_DOWNLOAD_ACCESS_DENIED(HttpStatus.FORBIDDEN, "FILE_DOWNLOAD_003", "파일에 접근할 권한이 없습니다", "필요한 권한을 확인하거나 관리자에게 문의하세요"),
    FILE_DOWNLOAD_EXPIRED(HttpStatus.BAD_REQUEST, "FILE_DOWNLOAD_004", "파일 다운로드 링크가 만료되었습니다", "새 다운로드 링크를 요청하세요"),
    FILE_DOWNLOAD_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_DOWNLOAD_005", "파일 다운로드 중 오류가 발생했습니다", "잠시 후 다시 시도하거나 관리자에게 문의하세요"),
    FILE_DOWNLOAD_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_DOWNLOAD_006", "서버 오류로 파일 다운로드에 실패했습니다", "잠시 후 다시 시도하거나 관리자에게 문의하세요"),
    FILE_DOWNLOAD_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "FILE_DOWNLOAD_007", "다운로드 횟수 제한을 초과했습니다", "나중에 다시 시도하거나 관리자에게 문의하세요"),
    FILE_DOWNLOAD_SIZE_ERROR(HttpStatus.BAD_REQUEST, "FILE_DOWNLOAD_008", "파일 크기 정보가 일치하지 않습니다", "파일이 손상되었을 수 있으니 관리자에게 문의하세요"),
    FILE_DOWNLOAD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "FILE_DOWNLOAD_009", "잘못된 다운로드 요청입니다", "올바른 파일 정보를 제공하세요")

    ;
    private final HttpStatus status;
    private final String code;  // 고유 코드
    private final String message;   // 메세지
    private final String addMessage;

}
