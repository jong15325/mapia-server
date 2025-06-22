package me.jjh.mapia.gameserver.controller;

import common.object.GameRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.gameserver.service.room.GameRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @GetMapping("/list")
    public Mono<ResponseEntity<List<GameRoom>>> list() {
        log.debug("[GAME ROOM CONTROLLER - list] START");

        List<GameRoom> dummyRooms = new ArrayList<>();

        log.debug("[GAME ROOM CONTROLLER - list] END");

        return gameRoomService.getAllRooms()
                .collectList()
                .doOnNext(rooms -> {
                    log.debug("[ROOM LIST] Redis에서 조회된 방 개수: {}", rooms.size());
                    rooms.forEach(room -> {
                        log.debug("[ROOM LIST] 방 정보 - ID: {}, 제목: {}, 인원: {}/{}, 상태: {}",
                                room.getRoomId(),
                                room.getRoomTitle(),
                                room.getRoomPlayerNum(),
                                room.getRoomMaxPlayerNum(),
                                room.getRoomStatus()
                        );
                    });
                })
                .map(rooms -> {
                    log.debug("[ROOM LIST] 응답 전송 - 총 {} 개 방", rooms.size());
                    return ResponseEntity.ok(rooms);
                })
                .doOnError(error -> {
                    log.error("[ROOM LIST] Redis 조회 실패: {}", error.getMessage(), error);
                })
                .onErrorReturn(ResponseEntity.status(500).body(Collections.emptyList()));
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<?>> create(@RequestBody GameRoom room) {

        log.debug("[GAME ROOM CONTROLLER - create] START");

        log.debug("[ROOM CREATE] roomId: {}", room.getRoomId());
        log.debug("[ROOM CREATE] title: {}, pwd: {}", room.getRoomTitle(), room.getRoomPwd());
        log.debug("[ROOM CREATE] players size: {}", room.getRoomPlayer() != null ? room.getRoomPlayer().size() : 0);

        if (room.getRoomPlayer() != null) {
            room.getRoomPlayer().forEach((player) -> {
                log.debug("  └ getPlayerIdx: {}", player.getPlayerIdx());
                log.debug("  └ getPlayerId: {}", player.getPlayerId());
                log.debug("  └ getPlayerRoomId: {}", player.getPlayerRoomId());
                log.debug("  └ isPlayerIsHost: {}", player.isPlayerIsHost());
                log.debug("  └ isPlayerIsReady: {}", player.isPlayerIsReady());
                log.debug("  └ getPlayerStatus: {}", player.getPlayerStatus());
                log.debug("  └ getPlayerJoinedAt: {}", player.getPlayerJoinedAt());
            });
        }

        log.debug("[GAME ROOM CONTROLLER - create] END");

        return gameRoomService.createRoom(room)
                .map(createdRoom -> ResponseEntity.ok(Map.of(
                        "common/status", 200,
                        "message", "방 생성 완료",
                        "roomId", createdRoom.getRoomId()
                )));
        //return ResponseEntity.ok(Map.of("status", 200, "message", "방 생성 및 브로드캐스트 완료"));
    }
}
