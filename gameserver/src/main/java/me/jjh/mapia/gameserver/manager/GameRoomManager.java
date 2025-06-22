package me.jjh.mapia.gameserver.manager;

import common.object.GamePlayer;
import common.object.GameRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.service.redis.RedisService;
import me.jjh.mapia.gameserver.util.DataVaildUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRoomManager {
    private final RedisService redisService;

    private static final String ROOM_KEY = "game:rooms";
    private static final String ROOM_PLAYER_KEY_PREFIX = "game:room:players:";
    private static final String ROOM_COUNTER_KEY = "game:room:counter";

    // 방 저장 (생성 or 수정)
    public Mono<Boolean> saveRoom(GameRoom room) {
        log.warn("[GAME ROOM MANAGER -  saveRoom] START");
        log.warn("[GAME ROOM MANAGER -  saveRoom] END");
        return redisService.hSet(ROOM_KEY, room.getRoomId(), room)
                .doOnNext(success -> {
                    if (success) {
                        log.info("[GAME ROOM MANAGER - saveRoom] 방 저장 성공 - roomId: {}", room.getRoomId());
                    } else {
                        log.error("[GAME ROOM MANAGER - saveRoom] 방 저장 실패 - roomId: {}", room.getRoomId());
                    }
                });
    }

    public Mono<GameRoom> getRoom(String roomId) {
        log.warn("[GAME ROOM MANAGER -  getRoom] START");
        log.warn("[GAME ROOM MANAGER -  getRoom] END");
        return redisService.hGet(ROOM_KEY, roomId).cast(GameRoom.class)
                .doOnNext(room -> log.debug("[GAME ROOM MANAGER - getRoom] 방 조회 성공 - roomId: {}", roomId));
    }

    public Mono<Long> deleteRoom(String roomId) {
        log.warn("[GAME ROOM MANAGER -  deleteRoom] START");
        log.warn("[GAME ROOM MANAGER -  deleteRoom] END");
        return deleteAllPlayersInRoom(roomId)
                .then(redisService.hDelete(ROOM_KEY, roomId));
    }

    public Flux<GameRoom> getAllRooms() {
        log.warn("[GAME ROOM MANAGER -  getAllRooms] START");
        log.warn("[GAME ROOM MANAGER -  getAllRooms] END");
        return redisService.hGetAll(ROOM_KEY).cast(GameRoom.class)
                .filter(room -> room != null)
                .doOnComplete(() -> log.debug("[GAME ROOM MANAGER -  getAllRooms] 전체 방 목록 조회 완료"));
    }

    public Mono<Boolean> savePlayer(GamePlayer player) {
        log.warn("[GAME ROOM MANAGER -  savePlayer] START");
        log.warn("[GAME ROOM MANAGER -  savePlayer] END");
        String playerKey = getPlayerListKey(player.getPlayerRoomId());

        return redisService.hSet(playerKey, player.getPlayerIdx().toString(), player)
                .doOnNext(success -> {
                    if (success) {
                        log.info("[GAME ROOM MANAGER -  savePlayer] 플레이어 저장 성공 - roomId: {}, playerIdx: {}",
                                player.getPlayerRoomId(), player.getPlayerIdx());
                    } else {
                        log.error("[GAME ROOM MANAGER -  savePlayer] 플레이어 저장 실패 - roomId: {}, playerIdx: {}",
                                player.getPlayerRoomId(), player.getPlayerIdx());
                    }
                });
    }

    // GamePlayer 단일 조회
    public Mono<GamePlayer> getPlayer(String roomId, Long playerIdx) {
        log.warn("[GAME ROOM MANAGER -  getPlayer] START");
        log.warn("[GAME ROOM MANAGER -  getPlayer] END");

        return redisService.hGet(getPlayerListKey(roomId), playerIdx.toString()).cast(GamePlayer.class)
                .doOnNext(player -> log.debug("[GAME ROOM MANAGER -  getPlayer] 플레이어 조회 성공 - roomId: {}, playerIdx: {}",
                        roomId, playerIdx));
    }

    // GamePlayer 전체 조회 (해당 방의 플레이어 목록)
    public Flux<GamePlayer> getAllPlayers(String roomId) {
        log.warn("[GAME ROOM MANAGER -  getAllPlayers] START");
        log.warn("[GAME ROOM MANAGER -  getAllPlayers] END");
        return redisService.hGetAll(getPlayerListKey(roomId)).cast(GamePlayer.class)
                .filter(player -> player != null)
                .doOnComplete(() -> log.debug("[GAME ROOM MANAGER -  getAllPlayers] 플레이어 목록 조회 완료 - roomId: {}", roomId));
    }

    //특정 방의 모든 플레이어 삭제
    public Mono<Boolean> deleteAllPlayersInRoom(String roomId) {
        log.warn("[GAME ROOM MANAGER -  deleteAllPlayersInRoom] START");
        log.warn("[GAME ROOM MANAGER -  deleteAllPlayersInRoom] END");
        if (DataVaildUtil.isStringEmpty(roomId)) {
            return Mono.just(false);
        }

        String playerKey = getPlayerListKey(roomId);

        return redisService.delete(playerKey)
                .doOnNext(deleted -> {
                    if (deleted) {
                        log.info("[GAME ROOM MANAGER -  deleteAllPlayersInRoom] 방의 모든 플레이어 삭제 성공 - roomId: {}", roomId);
                    } else {
                        log.debug("[GAME ROOM MANAGER -  deleteAllPlayersInRoom] 삭제할 플레이어가 없음 - roomId: {}", roomId);
                    }
                });
    }

    // 내부 키 생성 메서드
    private String getPlayerListKey(String roomId) {
        return ROOM_PLAYER_KEY_PREFIX + roomId;
    }

}
