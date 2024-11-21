package be.ucll.se.team15backend.chats.model;

import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    private String id;


    private LocalDateTime timestamp;

    @ManyToOne()
    @JsonBackReference
    private UserModel sender;

    @ManyToOne()
    @JsonManagedReference
    private Room room;


    private String data;

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserModel> readBy = new ArrayList<>();

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
