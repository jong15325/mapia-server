package me.jjh.mapia.webserver.util;

import me.jjh.mapia.webserver.common.response.AlertResponse;
import org.springframework.ui.Model;

/**
 * packageName    : me.jjh.mapia.util
 * fileName       : ReturnUtil
 * author         : JJH
 * date           : 2025-03-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-06        JJH       최초 생성
 */
public class ReturnUtil {
    public static String show(final Model model, AlertResponse response) {
        model.addAttribute("alertResponse", response);
        model.addAttribute("alertType", "SHOW");
        return "common/alert/alert";
    }

    public static String confirm(final Model model, AlertResponse response) {
        model.addAttribute("alertResponse", response);
        model.addAttribute("alertType", "CONFIRM");
        return "common/alert/alert";
    }

    public static String move(final Model model, AlertResponse response) {
        model.addAttribute("alertResponse", response);
        model.addAttribute("alertType", "MOVE");
        return "common/alert/alert";
    }

    public static String close(final Model model, AlertResponse response) {
        model.addAttribute("alertResponse", response);
        model.addAttribute("alertType", "CLOSE");
        return "common/alert/alert";
    }
}
