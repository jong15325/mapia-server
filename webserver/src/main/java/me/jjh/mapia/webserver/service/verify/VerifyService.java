package me.jjh.mapia.webserver.service.verify;

import me.jjh.mapia.webserver.dto.verify.VerifyDTO;

/**
 * packageName    : me.jjh.mapia.service.verification
 * fileName       : VerificationService
 * author         : JJH
 * date           : 2025-03-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        JJH       최초 생성
 */
public interface VerifyService {

    /**
     * 인증 정보 등록
     * @param verifyDTO
     */
    void insertVerify(VerifyDTO verifyDTO);

    /**
     * 인증 정보 조회(token)
     * @param token
     * @return
     */
    VerifyDTO findVerifyByToken(String token);

    /**
     * 인증 정보 활성화 여부 업데이트
     * @param verifyDTO
     */
    void updateVerifyActive(VerifyDTO verifyDTO);
    
}
