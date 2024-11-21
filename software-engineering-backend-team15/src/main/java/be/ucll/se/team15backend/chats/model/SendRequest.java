package be.ucll.se.team15backend.chats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendRequest {

    private String localId;
    private String roomId;
    private String data;
}
