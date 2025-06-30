package common.object;

import common.status.MessageType;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage {
    private MessageType type;
    private String action;
    private String senderId;
    private Object  data;
    private Long timestamp;
}
