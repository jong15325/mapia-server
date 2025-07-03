package me.jjh.mapia.gameserver.repository;

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
public class GameRoomRepository {
    private final RedisService redisService;

    private static final String ROOM_KEY = "game:rooms";

    // 방 생성
    public Mono<Boolean> createRoom(GameRoom room) {
        log.warn("[GAME ROOM REPOSITORY -  createRoom] START");
        log.warn("[GAME ROOM REPOSITORY -  createRoom] END");

        return redisService.hSet(ROOM_KEY, room.getRoomId(), room)
                .doOnError(error -> log.error("[GAME ROOM REPOSITORY - createRoom] 방 저장 오류 - roomId: {}, error: {}", room.getRoomId(), error.getMessage()));
    }

    // 방 검색
    public Mono<GameRoom> selectRoom(String roomId) {
        log.warn("[GAME ROOM REPOSITORY -  selectRoom] START");
        log.warn("[GAME ROOM REPOSITORY -  selectRoom] END");

        return redisService.hGet(ROOM_KEY, roomId)
                .cast(GameRoom.class)
                .doOnError(error -> log.error("[GAME ROOM REPOSITORY - selectRoom] 방 검색 오류 - roomId: {}, error: {}", roomId, error.getMessage()));
    }

    // 모든 방 검색
    public Flux<GameRoom> getAllRooms() {
        log.warn("[GAME ROOM REPOSITORY -  getAllRooms] START");
        log.warn("[GAME ROOM REPOSITORY -  getAllRooms] END");

        return redisService.hGetAll(ROOM_KEY)
                .cast(GameRoom.class)
                .filter(room -> room != null)
                .doOnNext(room -> log.debug("[GAME ROOM REPOSITORY - getAllRooms] 방 검색 성공"))
                .doOnComplete(() -> log.debug("[GAME ROOM REPOSITORY -  getAllRooms] 전체 방 목록 조회 완료"))
                .doOnError(error -> log.error("[GAME ROOM REPOSITORY - getAllRooms] 방 검색 오류 - error : {}", error.getMessage()));
    }

    // 방 업데이트
    public Mono<Boolean> updateRoom(GameRoom room) {
        log.warn("[GAME ROOM REPOSITORY -  updateRoom] START");
        log.warn("[GAME ROOM REPOSITORY -  updateRoom] END");

        return selectRoom(room.getRoomId())
                .hasElement() //존재 여부를 Boolean으로 변환
                .flatMap(exists -> {
                    if (!exists) {
                        log.warn("[GAME ROOM REPOSITORY -  updateRoom] 방이 존재하지 않습니다 - roomId: {}", room.getRoomId());
                        return Mono.just(false);
                    }

                    return redisService.hSet(ROOM_KEY, room.getRoomId(), room)
                            .doOnError(error -> log.error("[GAME ROOM REPOSITORY -  updateRoom] 방 업데이트 오류 - roomId: {}, error: {}", room.getRoomId(), error.getMessage()));
                })
                .doOnError(error -> log.error("[GAME ROOM REPOSITORY -  updateRoom] 방 업데이트 전 방 검색 오류 - roomId: {}, error: {}", room.getRoomId(), error.getMessage()));
    }

    // 방 삭제
    public Mono<Boolean> deleteRoom(String roomId) {
        log.warn("[GAME ROOM REPOSITORY -  deleteRoom] START");
        log.warn("[GAME ROOM REPOSITORY -  deleteRoom] END");

        return redisService.hDelete(ROOM_KEY, roomId)
                .map(deletedCount -> deletedCount > 0)// boolean 값으로 하기 위한 옵션에 불과
                .doOnError(error -> log.error("Error deleting room - roomId: {}, error: {}", roomId, error.getMessage()));
    }

    public Mono<Boolean> deleteAllRooms() {
        log.warn("[GAME ROOM REPOSITORY - deleteAllRooms] START");
        log.warn("[GAME ROOM REPOSITORY - deleteAllRooms] END");

        return redisService.delete(ROOM_KEY)
                .doOnError(error -> log.error("[GAME ROOM REPOSITORY - deleteAllRooms] 전체 방 삭제 오류 - error: {}", error.getMessage()));
    }

}
