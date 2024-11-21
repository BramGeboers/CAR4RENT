package be.ucll.se.team15backend.authentication.model;


import be.ucll.se.team15backend.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private UserModel user;
    private String token;

}
