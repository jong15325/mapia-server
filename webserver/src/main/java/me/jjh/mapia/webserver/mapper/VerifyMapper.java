package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 *packageName    : me.jjh.mapia.mapper
 * fileName       : VerificationMapper
 * author         : JJH
 * date           : 2025-03-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-02        JJH       최초 생성
 */

@Mapper
public interface VerifyMapper {

    /* SELECT */
    VerifyDTO findVerifyByToken(String token);

    /* INSERT */
    void insertVerify(VerifyDTO verifyDTO);

    /* UPDATE */
    void updateVerifyActive(VerifyDTO verifyDTO);

    /* DELETE */


}
