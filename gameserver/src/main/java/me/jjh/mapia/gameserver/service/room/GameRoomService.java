package me.jjh.mapia.gameserver.service.room;

import common.object.GamePlayer;
import common.object.GameRoom;
import common.status.RoomStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.repository.GameRoomRepository;
import me.jjh.mapia.gameserver.util.DataVaildUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * flatMap() - 각 방에 대해 비동기 처리
 * doOnNext() - 성공 시 로깅
 * doOnError() - 에러 시 로깅
 * doFinally() - 종료 시 로깅
 * count() - 결과 갯수
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;

    // 방 생성
    public Mono<GameRoom> createRoom(GameRoom room) {
        log.debug("[GAME ROOM SERVICE - createRoom] START");

        if (room == null || DataVaildUtil.isStringEmpty(room.getRoomId())) {
            log.warn("[GAME ROOM REPOSITORY - createRoom] room 객체에 문제가 발생했습니다");
            return Mono.empty();
        }

        String roomId = UUID.randomUUID().toString();

        room.setRoomId(roomId);
        room.setRoomPlayerNum(1);
        room.setRoomProgress(0);
        room.setRoomStatus(RoomStatus.WAITING);

        List<GamePlayer> players = new ArrayList<>();

        // 호스트 플레이어 설정
        if (!room.getRoomPlayer().isEmpty()) {
            GamePlayer hostPlayer = room.getRoomPlayer().get(0);
            hostPlayer.setPlayerRoomId(roomId);
            hostPlayer.setPlayerIsHost(true);
            hostPlayer.setPlayerIsReady(false); // 호스트는 기본적으로 준비 상태가 아님

            players.add(hostPlayer);
        }

        room.setRoomPlayer(players);

        log.debug("[GAME ROOM SERVICE - createRoom] END");

        return gameRoomRepository.createRoom(room)
                .flatMap(success -> {
                    if (success) {
                        log.info("[GAME ROOM SERVICE] 방 생성 성공 - roomId: {}, title: {}, maxPlayer: {}", room.getRoomId(), room.getRoomTitle(), room.getRoomMaxPlayerNum());
                        return Mono.just(room);
                    } else {
                        //log.error("[ROOM SERVICE] 방 생성 실패 - roomId: {}", roomId);
                        return Mono.error(new RuntimeException("방 생성에 실패했습니다."));
                    }
                })
                .doOnError(error -> log.error("방 생성 실패 - title: {}, error: {}", room.getRoomTitle(), error.getMessage()));
    }

    // 방 검색
    public Mono<GameRoom> selectRoom(String roomId) {
        log.debug("[GAME ROOM SERVICE - selectRoom] START");

        if (DataVaildUtil.isStringEmpty(roomId)) {
            log.warn("[GAME ROOM REPOSITORY - selectRoom] 선택된 방의 ID가 존재하지 않습니다 - roomId: {}", roomId);
            return Mono.empty();
        }

        log.debug("[GAME ROOM SERVICE - selectRoom] END");

        return gameRoomRepository.selectRoom(roomId)
                .doOnNext(room -> {
                    log.debug("[GAME ROOM SERVICE - selectRoom] 방 조회됨 - roomId: {}, title: {}, 플레이어: {}/{}",
                            room.getRoomId(),
                            room.getRoomTitle(),
                            room.getRoomPlayerNum(),
                            room.getRoomMaxPlayerNum());
                })
                .doOnError(error -> {
                    log.error("[GAME ROOM SERVICE - selectRoom] 방 조회 실패 - roomId: {}, error: {}", roomId, error.getMessage());
                })
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    log.warn("[GAME ROOM SERVICE - selectRoom] 방을 찾을 수 없음 - roomId: {}", roomId);
                }));
    }

    // 모든 방 검색
    public Flux<GameRoom> getAllRooms() {
        log.debug("[GAME ROOM SERVICE - getAllRooms] START");

        log.debug("[GAME ROOM SERVICE - getAllRooms] END");

        return gameRoomRepository.getAllRooms()
                .doOnNext(room -> {
                    log.debug("[GAME ROOM SERVICE - getAllRooms] 방 조회됨 - roomId: {}, title: {}, 플레이어: {}/{}",
                            room.getRoomId(),
                            room.getRoomTitle(),
                            room.getRoomPlayerNum(),
                            room.getRoomMaxPlayerNum());
                })
                .doOnError(error -> {
                    log.error("[GAME ROOM SERVICE - getAllRooms] 전체 방 목록 조회 실패: {}", error.getMessage(), error);
                })
                .filter(room -> room != null) // null 방 필터링
                .sort((room1, room2) -> {
                    // 생성 시간 내림차순 정렬 (최신 방이 위로)
                    return room2.getCreatedAt().compareTo(room1.getCreatedAt());
                });
    }

    // 방 업데이트
    public Mono<Boolean> updateRoom(GameRoom room) {
        log.debug("[GAME ROOM SERVICE - updateRoom] START");

        if (room == null || DataVaildUtil.isStringEmpty(room.getRoomId())) {
            log.warn("[GAME ROOM REPOSITORY - updateRoom] room 객체에 문제가 발생했습니다");
            return Mono.just(false);
        }

        log.debug("[GAME ROOM SERVICE - updateRoom] END");

        return gameRoomRepository.updateRoom(room)
                .doOnNext(updated -> {
                    if (updated) {
                        log.info("[GAME ROOM SERVICE] 방 업데이트 성공 - roomId: {}, title: {}", room.getRoomId(), room.getRoomTitle());
                    } else {
                        log.warn("[GAME ROOM SERVICE] 방 업데이트 실패 (방이 존재하지 않음) - roomId: {}", room.getRoomId());
                    }
                })
                .doOnError(error -> {
                    log.error("[GAME ROOM SERVICE] 방 업데이트 중 오류 발생 - roomId: {}, error: {}", room.getRoomId(), error.getMessage());
                });
    }

    // 방 삭제
    public Mono<Boolean> deleteRoom(String roomId) {
        log.debug("[GAME ROOM SERVICE - deleteRoom] START");

        if (DataVaildUtil.isStringEmpty(roomId)) {
            log.warn("[GAME ROOM REPOSITORY - deleteRoom] room 객체에 문제가 발생했습니다");
            return Mono.just(false);
        }

        log.debug("[GAME ROOM SERVICE - deleteRoom] END");

        return gameRoomRepository.deleteRoom(roomId)
                .doOnNext(deleted -> {
                    if (deleted) {
                        log.info("[GAME ROOM SERVICE - deleteRoom] 방 삭제 성공 - roomId: {}", roomId);
                    } else {
                        log.warn("[GAME ROOM SERVICE - deleteRoom] 방 삭제 실패 (방이 존재하지 않음) - roomId: {}", roomId);
                    }
                })
                .doOnError(error -> {
                    log.error("[GAME ROOM SERVICE] 방 삭제 중 오류 발생 - roomId: {}, error: {}", roomId, error.getMessage());
                });
    }

    public Mono<Boolean> deleteAllRooms() {
        log.debug("[GAME ROOM SERVICE - deleteAllRooms] START");
        log.debug("[GAME ROOM SERVICE - deleteAllRooms] END");

        return gameRoomRepository.deleteAllRooms()
                .doOnNext(deleted -> {
                    if (deleted) {
                        log.info("[ROOM SERVICE - deleteAllRooms] 모든 방 삭제 성공");
                    } else {
                        log.warn("[ROOM SERVICE - deleteAllRooms] 모든 방 삭제 실패");
                    }
                })
                .doOnError(error -> {
                    log.error("[GAME ROOM SERVICE] 모든 방 삭제 중 오류 발생 - error: {}", error.getMessage());
                });
    }
}
