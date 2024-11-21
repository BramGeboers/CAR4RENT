package be.ucll.se.team15backend.authentication.model;


import be.ucll.se.team15backend.user.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String email;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Long id;
}
