package me.jjh.mapia.gameserver.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import common.object.GameMessage;
import common.object.GamePlayer;
import common.object.GameRoom;
import common.status.PlayerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.manager.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameRoomHandler {

    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    // 간단한 방 저장소 (실제로는 DB 또는 Redis 사용)
    private final ConcurrentHashMap<String, GameRoom> gameRooms = new ConcurrentHashMap<>();

    /**
     * 방 생성 처리 (Reactive 방식)
     */
    public Mono<Void> handleCreateRoom(WebSocketSession session, GameMessage message) {
        try {
            String playerId = message.getSenderId();
            GamePlayer player = sessionManager.getConnectedPlayer(playerId);

            if (player == null) {
                return sendErrorToSession(session, "로그인이 필요합니다.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> roomData = (Map<String, Object>) message.getData();
            String roomTitle = (String) roomData.get("roomTitle");

            // 방 ID 생성 (실제로는 UUID 등 사용)
            String roomId = "ROOM_" + System.currentTimeMillis();

            // GameRoom 객체 생성
            GameRoom newRoom = new GameRoom();
            newRoom.setRoomId(roomId);
            newRoom.setRoomTitle(roomTitle);

            // 방에 호스트 플레이어 추가
            player.setPlayerRoomId(roomId);
            player.setPlayerIsHost(true);
            player.setPlayerStatus(PlayerStatus.WAITING);

            // 방 저장
            gameRooms.put(roomId, newRoom);

            // 방 생성 성공 응답
            GameMessage response = GameMessage.builder()
                    .action("ROOM_CREATED")
                    .data(Map.of(
                            "roomId", roomId,
                            "roomTitle", roomTitle,
                            "hostId", playerId,
                            "message", "방이 생성되었습니다."
                    ))
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("Room created - roomId: {}, host: {}", roomId, playerId);

            return sessionManager.sendToPlayer(playerId, response);

        } catch (Exception e) {
            log.error("Error creating room", e);
            return sendErrorToSession(session, "방 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 세션에 에러 메시지 전송 (Reactive 방식)
     */
    private Mono<Void> sendErrorToSession(WebSocketSession session, String errorMessage) {
        try {
            GameMessage error = GameMessage.builder()
                    .action("ERROR")
                    .data(errorMessage)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String jsonMessage = objectMapper.writeValueAsString(error);
            WebSocketMessage wsMessage = session.textMessage(jsonMessage);
            return session.send(Mono.just(wsMessage));
        } catch (Exception e) {
            log.error("Error sending error message", e);
            return Mono.error(e);
        }
    }
}