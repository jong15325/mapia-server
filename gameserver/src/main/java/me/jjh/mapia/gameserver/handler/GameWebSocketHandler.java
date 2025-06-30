package me.jjh.mapia.gameserver.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import common.object.GameMessage;
import common.object.GamePlayer;
import common.status.PlayerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.manager.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameWebSocketHandler implements WebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final GameRoomHandler gameRoomHandler;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("WebSocket connection established, sessionId: {}", session.getId());

        // 연결 성공 시 환영 메시지 전송
        GameMessage welcomeMessage = GameMessage.builder()
                .action("CONNECTED")
                .data("게임서버에 연결되었습니다.")
                .timestamp(System.currentTimeMillis())
                .build();

        Mono<Void> welcomeMono = sendMessage(session, welcomeMessage);

        // 메시지 수신 처리
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(payload -> handleMessage(session, payload))
                .doOnError(error -> log.error("Error handling message", error))
                .onErrorResume(error -> Mono.empty())
                .then();

        // 연결 종료 처리
        Mono<Void> cleanup = session.closeStatus()
                .doOnNext(closeStatus -> {
                    log.info("WebSocket connection closed, sessionId: {}, status: {}",
                            session.getId(), closeStatus);
                    // TODO: 세션 정리 로직
                })
                .then();

        return Mono.when(welcomeMono, input, cleanup);
    }

    /**
     * 메시지 처리
     */
    private Mono<Void> handleMessage(WebSocketSession session, String payload) {
        try {
            GameMessage gameMessage = objectMapper.readValue(payload, GameMessage.class);
            gameMessage.setTimestamp(System.currentTimeMillis());

            log.debug("Received message - action: {}, from: {}",
                    gameMessage.getAction(), gameMessage.getSenderId());

            // 액션별 처리 분기
            return switch (gameMessage.getAction()) {
                case "LOGIN" -> handleLogin(session, gameMessage);
                case "CREATE_ROOM" -> gameRoomHandler.handleCreateRoom(session, gameMessage);
                default -> {
                    log.warn("Unknown action: {}", gameMessage.getAction());
                    yield sendErrorMessage(session, "알 수 없는 액션입니다: " + gameMessage.getAction());
                }
            };

        } catch (Exception e) {
            log.error("Error parsing message", e);
            return sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 로그인 처리 - GamePlayer 객체 생성 및 세션 등록
     */
    private Mono<Void> handleLogin(WebSocketSession session, GameMessage message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> loginData = (Map<String, Object>) message.getData();

            String playerId = (String) loginData.get("playerId");
            Long playerIdx = Long.valueOf(loginData.get("playerIdx").toString());

            // GamePlayer 객체 생성
            GamePlayer player = new GamePlayer();
            player.setPlayerIdx(playerIdx);
            player.setPlayerId(playerId);
            player.setPlayerStatus(PlayerStatus.WAITING);

            // 세션 매니저에 등록
            sessionManager.addPlayerSession(player, session);

            // 로그인 성공 응답
            GameMessage response = GameMessage.builder()
                    .action("LOGIN_SUCCESS")
                    .data(Map.of(
                            "playerId", playerId,
                            "message", "로그인 성공"
                    ))
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Player logged in: {}", playerId);

            return sendMessage(session, response);

        } catch (Exception e) {
            log.error("Login error", e);
            return sendErrorMessage(session, "로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 세션에 메시지 전송 (Reactive 방식)
     */
    private Mono<Void> sendMessage(WebSocketSession session, GameMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            WebSocketMessage wsMessage = session.textMessage(jsonMessage);
            return session.send(Mono.just(wsMessage));
        } catch (Exception e) {
            log.error("Error sending message to session: {}", session.getId(), e);
            return Mono.error(e);
        }
    }

    /**
     * 에러 메시지 전송
     */
    private Mono<Void> sendErrorMessage(WebSocketSession session, String errorMessage) {
        GameMessage error = GameMessage.builder()
                .action("ERROR")
                .data(errorMessage)
                .timestamp(System.currentTimeMillis())
                .build();

        return sendMessage(session, error);
    }
}
