package common.object;


import common.status.PlayerStatus;
import lombok.Getter;
import lombok.Setter;

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
