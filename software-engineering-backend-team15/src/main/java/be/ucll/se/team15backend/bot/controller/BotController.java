package be.ucll.se.team15backend.bot.controller;

import be.ucll.se.team15backend.bot.model.BotException;
import be.ucll.se.team15backend.bot.model.BotResponse;
import be.ucll.se.team15backend.bot.service.BotService;
import be.ucll.se.team15backend.chats.model.ChatException;
import be.ucll.se.team15backend.security.JwtService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BotService botService;

    @GetMapping("")
    @SecurityRequirement(name = "bearerAuth")
    public BotResponse getBot(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam String message, @RequestParam String roomId) throws BotException, IOException, ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        return botService.main(message, email, roomId);
    }
}
