package me.jjh.mapia.webserver.service.verify.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.dto.verify.VerifyDTO;
import me.jjh.mapia.webserver.mapper.VerifyMapper;
import me.jjh.mapia.webserver.service.verify.VerifyService;
import org.springframework.stereotype.Service;

/**
 * packageName    : me.jjh.mapia.service.verification.impl
 * fileName       : VerificationServiceImpl
 * author         : JJH
 * date           : 2025-03-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class VerifyServiceImpl implements VerifyService {

    private final VerifyMapper verifyMapper;

    @Override
    public void insertVerify(VerifyDTO verifyDTO) {
        verifyMapper.insertVerify(verifyDTO);
    }

    @Override
    public VerifyDTO findVerifyByToken(String token) {
        return verifyMapper.findVerifyByToken(token);
    }

    @Override
    public void updateVerifyActive(VerifyDTO verifyDTO) {
        verifyMapper.updateVerifyActive(verifyDTO);
    }
}
