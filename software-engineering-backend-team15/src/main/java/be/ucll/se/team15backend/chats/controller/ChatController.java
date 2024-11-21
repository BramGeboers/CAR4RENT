package be.ucll.se.team15backend.chats.controller;

import be.ucll.se.team15backend.chats.model.*;
import be.ucll.se.team15backend.chats.service.ChatService;

import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ChatService chatService;

    @GetMapping("/overview")
    @SecurityRequirement(name = "bearerAuth")
    public List<RoomOverview> getRoomOverview(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return chatService.getOverview(email);

    }

    @PostMapping("/send")
    @SecurityRequirement(name = "bearerAuth")
    public void sendMessage(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestBody SendRequest sendRequest) throws ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        chatService.sendMessage(email, sendRequest.getRoomId(), sendRequest.getData(), sendRequest.getLocalId());
    }

    @GetMapping("/first")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatResponse> getFirstMessages(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam String roomId, @RequestParam Integer amount) throws ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        return chatService.getFirstMessage(email, roomId, amount);
    }

    @GetMapping("/next")
    @SecurityRequirement(name = "bearerAuth")
    public List<ChatResponse> getFirstMessages(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam String roomId, @RequestParam Integer amount, @RequestParam String startId) throws ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        return chatService.getNextMessages(email, roomId, amount, startId);
    }

    @GetMapping("/available")
    @SecurityRequirement(name = "bearerAuth")
    public Boolean isIdAvailable(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam String chatId) {
        return chatService.isIdAvailable(chatId);
    }

    @GetMapping("/room/{roomId}/emails")
    @SecurityRequirement(name = "bearerAuth")
    public List<String> getRoomEmails(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @PathVariable String roomId) throws ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        return chatService.getRoomEmails(roomId, email);
    }

    @PutMapping("/read")
    @SecurityRequirement(name = "bearerAuth")
    public void readMessages(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam String roomId) throws ChatException {
        String email = jwtService.extractEmail(token.substring(7));
        chatService.readMessages(email, roomId);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ChatException.class })
    public Map<String, String> handleServiceExceptions(ChatException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
