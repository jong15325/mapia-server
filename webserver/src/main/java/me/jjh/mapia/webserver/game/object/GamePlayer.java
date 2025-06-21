package me.jjh.mapia.webserver.game.object;

import lombok.Getter;
import lombok.Setter;
import me.jjh.mapia.webserver.dto.member.MemberDTO;
import me.jjh.mapia.webserver.game.status.PlayerStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class GamePlayer {
    private Long playerIdx;
    private String playerId;
    private String playerRoomId;
    private boolean playerIsReady = false;
    private boolean playerIsHost = false;
    private PlayerStatus playerStatus = PlayerStatus.WAITING;
    private LocalDateTime playerJoinedAt = LocalDateTime.now();
}
