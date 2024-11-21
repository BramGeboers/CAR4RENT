package be.ucll.se.team15backend.chats.repo;

import be.ucll.se.team15backend.chats.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, String> {


    Chat findChatById(String chatId);
    //delete by id
    void deleteChatById(String chat);

//    @Query("SELECT c FROM Chat c WHERE c.roomId = ?1 AND c.userId = ?2 AND c.read = false")
//    List<Chat> findUnreadChats(String roomId, Long userId);
}
