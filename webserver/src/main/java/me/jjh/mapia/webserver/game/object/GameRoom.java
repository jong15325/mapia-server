package me.jjh.mapia.webserver.game.object;

import lombok.Getter;
import lombok.Setter;
import me.jjh.mapia.webserver.game.status.RoomStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GameRoom {
    private String roomId; // 룸 id
    private String roomTitle; // 룸 제목
    private String roomPwd; // 룸 패스워드 (패스워드 존재 시 비밀방 없을 시 공개방)
    private List<GamePlayer> roomPlayer = new ArrayList<>(); // 현재 접속 인원 데이터
    private int roomPlayerNum; // 현재 접속 인원 수
    private int roomMaxPlayerNum; // 최대 접속 인원 수
    private int roomProgress; // 진행율
    private RoomStatus roomStatus = RoomStatus.WAITING;// 현재 룸 상태
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
