package be.ucll.se.team15backend.user.model;


import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements UserDetails {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Schema(defaultValue = "test@ucll.be")
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 4)
    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean locked;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @Builder.Default
    @JsonIgnore
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Chat> sendChats = new ArrayList<>();

    @ManyToMany(mappedBy = "readBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Chat> readChats = new ArrayList<>();


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.name()));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }



}
