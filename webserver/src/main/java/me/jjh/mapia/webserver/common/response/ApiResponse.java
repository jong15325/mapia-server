package me.jjh.mapia.webserver.common.response;

import lombok.*;
import me.jjh.mapia.webserver.common.code.Code;
import me.jjh.mapia.webserver.common.code.DateFormatCode;
import me.jjh.mapia.webserver.common.code.ErrorCode;
import me.jjh.mapia.webserver.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : me.jjh.mapia.common.response
 * fileName       : ApiResponse
 * author         : JJH
 * date           : 2025-02-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-21        JJH       최초 생성
 */

@RequiredArgsConstructor //NoArgsConstructor 기본생성자, AllArgsConstructor 모든 필드 생성자, RequiredArgsConstructor final/@NonNull 생성자
@Getter
public class ApiResponse<T> {
    private final HttpStatus status; // 상태 코드
    private final String code; // 공통 코드
    private final String message; // 응답 메세지
    private final String addMessage; // 추가 메세지
    private final String moveUrl; // 이동 url
    private final T data; // 데이터
    private final String timestamp;  // 응답 생성 시간

    // 성공 응답 생성 (ComCode 활용)
    public static <T> ApiResponse<T> success(Code code) {
        return new ApiResponse<>(
                HttpStatus.OK,
                code.getCode(),
                code.getMessage(),
                code.getAddMessage(),
                null,
                null,
                DateUtil.getNow(DateFormatCode.STANDARD)
        );
    }

    // 성공 응답 생성 (ComCode 활용)
    public static <T> ApiResponse<T> success(Code code, T data) {
        return new ApiResponse<>(
                HttpStatus.OK,
                code.getCode(),
                code.getMessage(),
                code.getAddMessage(),
                null,
                data,
                DateUtil.getNow(DateFormatCode.STANDARD)
        );
    }

    // 성공 응답 생성 (ComCode 활용)
    public static <T> ApiResponse<T> success(Code code, T data, String moveUrl) {
        return new ApiResponse<>(
                HttpStatus.OK,
                code.getCode(),
                code.getMessage(),
                code.getAddMessage(),
                moveUrl,
                data,
                DateUtil.getNow(DateFormatCode.STANDARD)
        );
    }

    public static <T> ApiResponse<T> error(Code code) {
        return new ApiResponse<>(
                code.getStatus(),
                code.getCode(),
                code.getMessage(),
                code.getAddMessage(),
                null,
                null,
                DateUtil.getNow(DateFormatCode.STANDARD)
        );
    }
}
