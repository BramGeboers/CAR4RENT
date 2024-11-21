package be.ucll.se.team15backend.chats.model;

import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {



        @Id
        private String roomId;
        private String roomName;

        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonManagedReference
        @Builder.Default
        private List<Chat> chats = new ArrayList<>();

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonIgnore
        @Builder.Default
        private List<UserModel> users = new ArrayList<>();

        public void addChat(Chat chat) {
                chats.add(chat);
        }

        public void addUser(UserModel user) {
                users.add(user);
        }

        public void removeUser(UserModel user) {
                users.remove(user);
        }


        public String getLastMessage() {
                if (chats.isEmpty()) {
                        return "";
                }

                return chats.get(chats.size() - 1).getData();
        }

        public String getLastMessageSender() {
                if (chats.isEmpty()) {
                        return "";
                }

                return chats.get(chats.size() - 1).getSender().getUsername();
        }

        public String getLastMessageTimestamp() {
                if (chats.isEmpty()) {
                        return "";
                }

                return chats.get(chats.size() - 1).getTimestamp().toString();
        }

        public int getUnreadMessages(UserModel user) {
                int unreadMessages = 0;
                List<Chat> chats = this.getChats();
                for (Chat chat : chats) {
                        if (!chat.getReadBy().contains(user)) {
                                unreadMessages++;
                        }
                }
                return unreadMessages;
        }

        @Override
        public String toString() {
                return "Room{" +
                        "roomId='" + roomId + '\'' +
                        ", roomName='" + roomName + '\'' +
                        '}';
        }
}
