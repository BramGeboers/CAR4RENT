package be.ucll.se.team15backend.bot.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Service
public class StringFunctions {


    public String executeFunction(String functionName, String email) {
        switch (functionName) {
            case "time":
                return this.getTime();
            case "email":
                return email;
            case "user":
                return this.getUser(email);
            // Add more functions here

            default:
                return functionName + "_ERROR";
        }

    }


    private String getTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String getUser(String email) {
        return capitalizeFirstLetter(email.split("@")[0].replace(".", " "));
    }

    private String capitalizeFirstLetter(String sentence) {
            String[] words = sentence.split("\\s+");
            StringBuilder result = new StringBuilder();

            for (String word : words) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase()).append(" ");
            }
        return result.toString().trim();
    }
}
