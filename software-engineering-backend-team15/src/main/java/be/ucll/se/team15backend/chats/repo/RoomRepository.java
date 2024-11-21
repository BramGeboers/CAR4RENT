package be.ucll.se.team15backend.chats.repo;

import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {


    Room findRoomByRoomId(String roomId);

//    select count(*) from room_chats r left outer join chat_read_by c on r.chats_id = c.chat_id and c.read_by_user_id = 4
//    where r.room_room_id = 'general' and c.read_by_user_id is null
//    @Query("SELECT COUNT(r) FROM Chat r LEFT JOIN r.readBy c WHERE r.room.roomId = :roomId AND (c IS NULL OR c.userId != :userId)")
    @Query("SELECT COUNT(c) FROM Chat c WHERE c.room.roomId = :roomId AND (:userId NOT IN (SELECT r.userId from c.readBy r))")
    Integer getUnreadMessagesCount(String roomId, Long userId);

//    select c.* from chat c left outer join chat_read_by r on c.id = r.chat_id and r.read_by_user_id = 4 where room_room_id = 'general' and r.read_by_user_id is null
//    @Query("SELECT c FROM Chat c LEFT OUTER JOIN c.readBy r ON c.id = WHERE c.room.roomId = :roomId AND r.userId = :userId")
    @Query("SELECT c FROM Chat c WHERE c.room.roomId = :roomId AND (:userId NOT IN (SELECT r.userId from c.readBy r))")
    List<Chat> getUnreadMessages(String roomId, Long userId);



//    Integer countAllByRoomIdAndReadByUserIdIsNull(String roomId, Long userId);
}
