package be.ucll.se.team15backend.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotResponse {

    private String id;
    private LocalDateTime timestamp;
    private String senderEmail;
    private String roomId;
    private String data;
}
