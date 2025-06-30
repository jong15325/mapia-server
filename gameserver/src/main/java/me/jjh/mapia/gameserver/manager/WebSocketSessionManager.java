package me.jjh.mapia.gameserver.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.object.GameMessage;
import common.object.GamePlayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketSessionManager {
    private final ObjectMapper objectMapper;

    // playerId -> WebSocketSession 매핑
    private final ConcurrentHashMap<String, WebSocketSession> playerSessions = new ConcurrentHashMap<>();

    // playerId -> GamePlayer 매핑 (업로드하신 객체 활용)
    private final ConcurrentHashMap<String, GamePlayer> connectedPlayers = new ConcurrentHashMap<>();

    /**
     * 플레이어 세션 추가
     */
    public void addPlayerSession(GamePlayer player, WebSocketSession session) {
        String playerId = player.getPlayerId();

        playerSessions.put(playerId, session);
        connectedPlayers.put(playerId, player);

        log.info("Player session added: {}, total sessions: {}",
                playerId, playerSessions.size());
    }

    /**
     * 플레이어 세션 제거
     */
    public void removePlayerSession(String playerId) {
        playerSessions.remove(playerId);
        connectedPlayers.remove(playerId);

        log.info("Player session removed: {}, total sessions: {}",
                playerId, playerSessions.size());
    }

    /**
     * 특정 플레이어에게 메시지 전송 (Reactive 방식)
     */
    public Mono<Void> sendToPlayer(String playerId, GameMessage message) {
        WebSocketSession session = playerSessions.get(playerId);

        if (session != null && !session.isOpen()) {
            log.warn("Session is closed for player: {}", playerId);
            return Mono.empty();
        }

        if (session != null) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                WebSocketMessage wsMessage = session.textMessage(jsonMessage);
                return session.send(Mono.just(wsMessage));
            } catch (Exception e) {
                log.error("Error sending message to player: {}", playerId, e);
                return Mono.error(e);
            }
        }

        log.warn("Cannot send message to player {}: session not found", playerId);
        return Mono.empty();
    }

    /**
     * 연결된 플레이어 정보 조회
     */
    public GamePlayer getConnectedPlayer(String playerId) {
        return connectedPlayers.get(playerId);
    }

    /**
     * 전체 연결된 플레이어 수
     */
    public int getTotalConnectedPlayers() {
        return playerSessions.size();
    }

    /**
     * 세션 조회
     */
    public WebSocketSession getSession(String playerId) {
        return playerSessions.get(playerId);
    }
}
