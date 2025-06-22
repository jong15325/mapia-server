package me.jjh.mapia.gameserver.service.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.manager.GameRoomManager;
import me.jjh.mapia.gameserver.object.GamePlayer;
import me.jjh.mapia.gameserver.object.GameRoom;
import me.jjh.mapia.gameserver.status.RoomStatus;
import me.jjh.mapia.gameserver.util.DataVaildUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomManager gameRoomManager;

    // 방 생성
    public Mono<GameRoom> createRoom(GameRoom room) {
        log.debug("[GAME ROOM SERVICE - createRoom] START");
        log.debug("[GAME ROOM SERVICE - createRoom] END");

        return gameRoomManager.saveRoom(room)
                .flatMap(success -> {
                    if (success) {
                        return setupNewRoom(room);
                    } else {
                        //log.error("[ROOM SERVICE] 방 생성 실패 - roomId: {}", roomId);
                        return Mono.error(new RuntimeException("방 생성에 실패했습니다."));
                    }
                });
    }

    // 방 조회
    public Mono<GameRoom> getRoom(String roomId) {
        log.debug("[GAME ROOM SERVICE - getRoom] START");

        if (DataVaildUtil.isStringEmpty(roomId)) {
            log.warn("[ROOM SERVICE] 잘못된 roomId로 조회 시도: {}", roomId);
            return Mono.empty();
        }

        log.debug("[ROOM SERVICE] 방 조회 시작 - roomId: {}", roomId);

        log.debug("[GAME ROOM SERVICE - getRoom] END");

        return gameRoomManager.getRoom(roomId)
                .doOnNext(room -> {
                    if (room != null) {
                        log.debug("[ROOM SERVICE] 방 조회 성공 - roomId: {}, title: {}", room.getRoomId(), room.getRoomTitle());
                    }
                })
                .doOnError(error -> {
                    log.error("[ROOM SERVICE] 방 조회 실패 - roomId: {}, error: {}", roomId, error.getMessage());
                })
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    log.warn("[ROOM SERVICE] 방을 찾을 수 없음 - roomId: {}", roomId);
                }));
    }

    public Flux<GameRoom> getAllRooms() {
        log.debug("[GAME ROOM SERVICE - getAllRooms] START");

        log.debug("[GAME ROOM SERVICE - getAllRooms] END");

        return gameRoomManager.getAllRooms()
                .doOnNext(room -> {
                    log.debug("[ROOM SERVICE] 방 조회됨 - roomId: {}, title: {}, 플레이어: {}/{}",
                            room.getRoomId(),
                            room.getRoomTitle(),
                            room.getRoomPlayerNum(),
                            room.getRoomMaxPlayerNum());
                })
                .doOnComplete(() -> {
                    log.info("[ROOM SERVICE] 전체 방 목록 조회 완료");
                })
                .doOnError(error -> {
                    log.error("[ROOM SERVICE] 전체 방 목록 조회 실패: {}", error.getMessage(), error);
                })
                .filter(room -> room != null) // null 방 필터링
                .sort((room1, room2) -> {
                    // 생성 시간 내림차순 정렬 (최신 방이 위로)
                    return room2.getCreatedAt().compareTo(room1.getCreatedAt());
                });
    }

    // 방 삭제
    public Mono<Boolean> deleteRoom(String roomId) {
        log.debug("[GAME ROOM SERVICE - deleteRoom] START");

        if (DataVaildUtil.isStringEmpty(roomId)) {
            log.warn("[ROOM SERVICE] 잘못된 roomId로 삭제 시도: {}", roomId);
            return Mono.just(false);
        }

        log.info("[ROOM SERVICE] 방 삭제 시작 - roomId: {}", roomId);

        log.debug("[GAME ROOM SERVICE - deleteRoom] END");

        return gameRoomManager.deleteRoom(roomId)
                .map(count -> {
                    boolean deleted = count > 0;
                    if (deleted) {
                        log.info("[ROOM SERVICE] 방 삭제 성공 - roomId: {}", roomId);
                    } else {
                        log.warn("[ROOM SERVICE] 방 삭제 실패 (방이 존재하지 않음) - roomId: {}", roomId);
                    }
                    return deleted;
                })
                .doOnError(error -> {
                    log.error("[ROOM SERVICE] 방 삭제 중 오류 발생 - roomId: {}, error: {}", roomId, error.getMessage());
                });
    }

    // 활성 방 개수 조회
    public Mono<Long> getActiveRoomsCount() {
        log.debug("[ROOM SERVICE] 활성 방 개수 조회");

        return getAllRooms()
                .filter(room -> room.getRoomStatus() != null &&
                        room.getRoomStatus() != RoomStatus.FINISHED)
                .count()
                .doOnNext(count -> {
                    log.debug("[ROOM SERVICE] 활성 방 개수: {}", count);
                });
    }

    // 특정 조건으로 방 검색
    public Flux<GameRoom> searchRooms(String keyword) {
        if (DataVaildUtil.isStringEmpty(keyword)) {
            return getAllRooms();
        }

        log.info("[ROOM SERVICE] 방 검색 시작 - keyword: {}", keyword);

        return getAllRooms()
                .filter(room -> room.getRoomTitle() != null &&
                        room.getRoomTitle().toLowerCase().contains(keyword.toLowerCase()))
                .doOnNext(room -> {
                    log.debug("[ROOM SERVICE] 검색된 방 - roomId: {}, title: {}", room.getRoomId(), room.getRoomTitle());
                })
                .doOnComplete(() -> {
                    log.info("[ROOM SERVICE] 방 검색 완료 - keyword: {}", keyword);
                });
    }

    /**
     * 새 방 설정
     */
    private Mono<GameRoom> setupNewRoom(GameRoom room) {
        String roomId = UUID.randomUUID().toString();

        room.setRoomId(roomId);
        room.setRoomPlayerNum(1);
        room.setRoomMaxPlayerNum(8);
        room.setRoomProgress(0);
        room.setRoomStatus(RoomStatus.WAITING);

        // 호스트 플레이어 설정
        if (!room.getRoomPlayer().isEmpty()) {
            GamePlayer hostPlayer = room.getRoomPlayer().get(0);
            hostPlayer.setPlayerRoomId(roomId);
            hostPlayer.setPlayerIsHost(true);
            hostPlayer.setPlayerIsReady(false); // 호스트는 기본적으로 준비 상태가 아님
        }

        return Mono.just(room);
    }
}
