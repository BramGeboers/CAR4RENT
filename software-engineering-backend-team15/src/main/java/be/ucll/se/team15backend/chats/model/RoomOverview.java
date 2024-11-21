package be.ucll.se.team15backend.chats.model;

import be.ucll.se.team15backend.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomOverview {

    private String roomId;
    private String roomName;
    private String lastMessage;
    private String lastMessageSender;
    private String lastMessageTimestamp;
    private int unreadMessages;


    public static RoomOverview fromRoom(Room room, UserModel user) {
        return RoomOverview.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .lastMessage(room.getLastMessage())
                .lastMessageSender(room.getLastMessageSender())
                .lastMessageTimestamp(room.getLastMessageTimestamp())
//                .unreadMessages(room.getUnreadMessages(user))
                .build();
    }

    
}
