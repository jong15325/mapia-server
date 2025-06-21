package me.jjh.mapia.webserver.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : me.jjh.mapia.controller
 * fileName       : AlertController
 * author         : JJH
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        JJH       최초 생성
 */

@Slf4j
@RestController
public class AlertController {

    @PostMapping("/clearAlert")
    public ResponseEntity<Void> clearAlert(HttpSession session, @RequestParam("sessionName") String sessionName) {

        log.debug("[ALERT CONTROLLER] CLEAR ALERT START");

        session.removeAttribute(sessionName);

        log.debug("[ALERT CONTROLLER] CLEAR ALERT END");

        return ResponseEntity.ok().build();
    }
}
