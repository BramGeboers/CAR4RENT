package be.ucll.se.team15backend.chats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private String id;
    private String timestamp;
    private String senderEmail;
    private String data;

}
