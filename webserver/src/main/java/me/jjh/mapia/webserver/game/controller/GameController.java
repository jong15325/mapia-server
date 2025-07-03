package me.jjh.mapia.webserver.game.controller;

import common.object.GamePlayer;
import common.object.GameRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.common.code.AlertType;
import me.jjh.mapia.webserver.common.code.ComCode;
import me.jjh.mapia.webserver.common.code.RedisKeyCode;
import me.jjh.mapia.webserver.common.response.AlertResponse;
import me.jjh.mapia.webserver.properties.GameServerProperties;
import me.jjh.mapia.webserver.response.member.MemberResDTO;
import me.jjh.mapia.webserver.util.DateUtil;
import me.jjh.mapia.webserver.util.ReturnUtil;
import me.jjh.mapia.webserver.util.SecurityUtil;
import me.jjh.mapia.webserver.util.UserSessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/game/rooms")
public class GameController {

    private final UserSessionUtil userSessionUtil;
    private final RestTemplate restTemplate;
    private final GameServerProperties gameServerProperties;
    private final WebClient gameWebClient;

    /**
     * 마피아 게임룸 리스트
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {

        log.debug("[GAME CONTROLLER - list] START");

        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();

        if (userOpt.isEmpty()) {
            return ReturnUtil.move(model, new AlertResponse(ComCode.LOGIN_SESSION_EXPIRED, AlertType.WARNING, "/rooms/list"));
        }

        try {

            GameRoom[] rooms = gameWebClient.get()
                    .uri("/api/rooms/list")
                    .retrieve()
                    .bodyToMono(GameRoom[].class) // 이부분은 역직렬화
                    .block(); // MVC에서는 block() 사용 가능

            MemberResDTO currentUser = userOpt.get();

            // 게임서버 접속용 JWT 토큰 발급 (RedisKeyCode 기반 만료시간)
            String gameAccessToken = SecurityUtil.generateGameAccessToken(currentUser);

            // 토큰 만료시간 정보 (클라이언트에서 토큰 갱신 판단용)
            long expAt = RedisKeyCode.GAME_SOCKET_ACCESS.getDuration().toMinutes();


            model.addAttribute("rooms", Arrays.asList(rooms != null ? rooms : new GameRoom[0]));
            model.addAttribute("apiHost", gameServerProperties.getServerAddress());
            model.addAttribute("gameAccessToken", gameAccessToken);
            model.addAttribute("expAt", expAt);

        } catch (Exception e) {
            model.addAttribute("rooms", Collections.emptyList());
        }

        log.debug("[GAME CONTROLLER - list] END");

        return "game/room_list";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestBody GameRoom room) {
        Optional<MemberResDTO> userOpt = userSessionUtil.getCurrentUserSession();

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("세션 만료");
        }

        MemberResDTO user = userOpt.get();

        // Player 정보 세팅
        GamePlayer player = new GamePlayer();
        player.setPlayerIdx(user.getMemberIdx());
        player.setPlayerId(user.getMemberId());
        player.setPlayerIsHost(true);

        List<GamePlayer> roomPlayer = new ArrayList<>(); // 현재 접속 인원 데이터
        roomPlayer.add(player);
        room.setRoomPlayer(roomPlayer);

        try {
            String response = gameWebClient.post()
                    .uri("/api/rooms/create")
                    .bodyValue(room)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create room", e);
            return ResponseEntity.status(500).body("게임서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/deleteAllRooms")
    @ResponseBody
    public ResponseEntity<?> deleteAllRooms() {
        try {
            String response = gameWebClient.post()
                    .uri("/api/rooms/deleteAllRooms")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create room", e);
            return ResponseEntity.status(500).body("게임서버 오류가 발생했습니다.");
        }
    }
}
