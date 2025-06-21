package me.jjh.mapia.webserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {

        log.debug("[MAIN CONTROLLER - index] START");

        log.debug("[MAIN CONTROLLER - index] END");

        return "index";
    }
}
