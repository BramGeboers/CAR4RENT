package be.ucll.se.team15backend.bot.service;

import be.ucll.se.team15backend.bot.model.BotException;
import be.ucll.se.team15backend.bot.model.BotResponse;
import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.ChatException;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.model.MapException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotService {

    private final String secret = "HlqtOL-sTXg.iHt3UskTq5nOuf-RM-pkccGEgO0GgEp0ONaI1MuppuM";  // This is a secret key for the bot

    private final String url = "https://directline.botframework.com/v3/directline/conversations/6zL1XOE2PBPH15kP3jKLmh-eu/activities";


    @Autowired
    private ChatService chatService;

    @Autowired
    private StringFunctions stringFunctions;


    public BotResponse main(String message, String email, String roomId) throws IOException, BotException, ChatException {
        String responsId = UUID.randomUUID().toString();

        String rawResponse = askBot(message, email, roomId);

        String response = this.convertResponse(rawResponse, email);

        Chat chat = chatService.sendMessage("boxter.buddy@ucll.be", roomId, response, responsId);

        BotResponse returnResponse = BotResponse.builder()
                .id(responsId)
                .timestamp(chat.getTimestamp())
                .senderEmail("boxter.buddy@ucll.be")
                .roomId(roomId)
                .data(response)
                .build();

        return returnResponse;

    }

    public String askBot(String message, String email, String roomId) throws IOException, BotException, ChatException {


        if (!chatService.emailInRoom(email, roomId)) {
            throw new BotException("room", "User is not in the room");
        }

        String id;
        try {
            id = postRequest(message, email);
        } catch (BotException e) {
            System.out.println(e.getMessage());
            return "An error occurred, try again later.";
        }



        String response;
        try {
            response = getResponse(id);
        } catch (BotException e) {
            response = "An error occurred, try again later.";
            System.out.println(e.getMessage());
        }

        return response;
    }

    private String getResponse(String id) throws IOException, BotException {
        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Authorization", "Bearer " + secret);

        int statusCode = connection.getResponseCode();
        if (statusCode == 502) {
            throw new BotException("request", "Bad Gateway (502)");
        } else if (statusCode != 200) {
            throw new BotException("error", "Unexpected status code: " + statusCode);
        }

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(connection.getInputStream());
        JsonNode activitiesNode = rootNode.get("activities");

        // Iterate through activities to find the one with the specified ID
        for (JsonNode activityNode : activitiesNode) {
            JsonNode messageIdNode = activityNode.get("replyToId");
            if (messageIdNode != null && messageIdNode.asText().equals(id)) {
                JsonNode textNode = activityNode.get("text");
                if (textNode != null) {
                    return textNode.asText();
                }
            }
        }

        // If the specified ID is not found, return null or throw an exception as needed
        throw new BotException("id", "No activity found with ID: " + id);
    }

    private String postRequest(String message, String email) throws IOException, BotException {
        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("Authorization", "Bearer " + secret);

        // Enable output for the request body
        connection.setDoOutput(true);

        // Construct the request body JSON
        String requestBody = "{\"type\": \"message\", \"text\": \"" + message + "\", \"from\": {\"id\": \"" + email + "\"}}";

        // Write request body to output stream
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.writeBytes(requestBody);
            outputStream.flush();
        }

        int statusCode = connection.getResponseCode();
        if (statusCode != 200) {
            throw new BotException("error", "Unexpected status code: " + statusCode);
        }

        // Read response body
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.toString());
        JsonNode idNode = rootNode.get("id");

        if (idNode != null) {
            return idNode.asText();
        } else {
            throw new BotException("error", "ID not found in response");
        }

    }



    private String convertResponse(String input, String email) throws IOException, ChatException {
        StringBuffer result = new StringBuffer();
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = stringFunctions.executeFunction(variable, email);
            matcher.appendReplacement(result, replacement != null ? Matcher.quoteReplacement(replacement) : "");
        }

        matcher.appendTail(result);
        return result.toString();
    }




}
