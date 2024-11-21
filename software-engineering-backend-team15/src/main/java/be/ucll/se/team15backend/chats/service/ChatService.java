package be.ucll.se.team15backend.chats.service;

import be.ucll.se.team15backend.chats.model.*;
import be.ucll.se.team15backend.chats.repo.ChatRepository;
import be.ucll.se.team15backend.chats.repo.RoomRepository;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {


    @Autowired
    private UserService userService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private EntityManager entityManager;

    public List<RoomOverview> getOverview(String email) {

        UserModel user = userService.getUserByEmail(email);

        List<Room> rooms = user.getRooms();

        List<RoomOverview> roomOverviews = new ArrayList<>();
        for (Room room : rooms) {
            RoomOverview roomOverview= RoomOverview.fromRoom(room, user); // without unread messages
//            roomOverview.setUnreadMessages(0);
            roomOverview.setUnreadMessages(roomRepository.getUnreadMessagesCount(room.getRoomId(), user.getUserId()));
            roomOverviews.add(roomOverview);
        }

        return roomOverviews;

    }

    public void initRooms(UserModel user) {

        if (user == null) {
            return;
        }

        if (user.getRole().equals(Role.BOT)) {
            return;
        }

        Room general = roomRepository.findRoomByRoomId("general");

        general.addUser(user);
        roomRepository.save(general);

        Room botRoom = Room.builder()
                .roomId("bot-" + user.getUserId())
                .roomName("Boxter Buddy")
                .build();

        botRoom.addUser(user);
//        List<UserModel> users = userService.getAllUsers();
        UserModel bot = userService.getBot();
        botRoom.addUser(bot);

        Chat welcomeBotChat = Chat.builder()
                .id(UUID.randomUUID().toString())
                .data("Welcome to Cars4Rent! I'm Boxter Buddy, here to assist you with any questions you might have.")
                .sender(userService.getBot())
                .room(botRoom)
                .build();

        saveRoom(botRoom);
        saveChat(welcomeBotChat);


        botRoom.addChat(welcomeBotChat);
        saveRoom(botRoom);


        roomRepository.save(botRoom);
        user.addRoom(botRoom);
        user.addRoom(general);
        userService.updateUser(user);
    }


    public Chat saveChat(Chat chat) {
        if (chat.getId() == null) {
            chat.setId(UUID.randomUUID().toString());
        }
        chat.setTimestamp(LocalDateTime.now());
        if (entityManager != null) {
            entityManager.clear();  // IDK WAT DIT DOET MAAR HET WERKT NIET ZONDER, DUURDE MIJ 3 UUR OM TE FIXE DUS BLIJF ER VANAF!! groetjes Rein
        }
        return chatRepository.save(chat);
    }

    public Room saveRoom(Room room) {
        if (room.getRoomId() == null) {
            room.setRoomId(UUID.randomUUID().toString());
        }
        return roomRepository.save(room);
    }


    public Chat sendMessage(String email, String roomId, String message, String localId) throws ChatException {
        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            throw new ChatException("user", "User not found");
        }

        if (room == null) {
            throw new ChatException("room", "Room not found");
        }

        if (!room.getUsers().contains(user)) {
            throw new ChatException("room", "User not in room");
        }

        if (message == null || message.isEmpty()) {
            throw new ChatException("message", "Message cannot be empty");
        }

        String id;
        if (localId == null) {
            id = UUID.randomUUID().toString();
        } else {
            if (!isIdAvailable(localId)) {
                throw new ChatException("localId", "Id already exists");
            }
            id = localId;
        }
        Chat chat = Chat.builder()
                .id(id)
                .data(message)
                .sender(user)
                .room(room)
                .readBy(List.of(user))
                .build();

        room.addChat(chat);
        Chat returnChat = saveChat(chat);
        saveRoom(room);

        return returnChat;
    }



    public List<ChatResponse> getFirstMessage(String email, String roomId, Integer amount) throws ChatException {
        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            throw new ChatException("user", "User not found");
        }

        if (room == null) {
            throw new ChatException("room", "Room not found");
        }

        if (!room.getUsers().contains(user)) {
            throw new ChatException("room", "User not in room");
        }

        List<Chat> chats = room.getChats();
        List<ChatResponse> result = new ArrayList<>();

        for (int i = chats.size() - 1; i >= 0; i--) {
            if (result.size() >= amount) {
                break;
            }
            Chat chat = chats.get(i);
            ChatResponse chatResponse = ChatResponse.builder()
                    .id(chat.getId())
                    .timestamp(chat.getTimestamp().toString())
                    .senderEmail(chat.getSender().getEmail())
                    .data(chat.getData())
                    .build();
            result.add(chatResponse);
        }

        return result;
    }

    public List<ChatResponse> getNextMessages(String email, String roomId, Integer amount, String startId) throws ChatException {
        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            throw new ChatException("user", "User not found");
        }

        if (room == null) {
            throw new ChatException("room", "Room not found");
        }

        if (!room.getUsers().contains(user)) {
            throw new ChatException("room", "User not in room");
        }

        List<Chat> chats = room.getChats();
        List<ChatResponse> result = new ArrayList<>();

        boolean start = false;
        for (int i = chats.size() - 1; i >= 0; i--) {
            Chat chat = chats.get(i);
            if (start) {
                ChatResponse chatResponse = ChatResponse.builder()
                        .id(chat.getId())
                        .timestamp(chat.getTimestamp().toString())
                        .senderEmail(chat.getSender().getEmail())
                        .data(chat.getData())
                        .build();
                result.add(chatResponse);
                if (result.size() >= amount) {
                    break;
                }
            } else {
                if (chat.getId().equals(startId)) {
                    start = true;
                }
            }
        }

        return result;
    }

    public Boolean isIdAvailable(String chatId) {
        return chatRepository.findChatById(chatId) == null;
    }

    public List<String> getRoomEmails(String roomId, String email) throws ChatException {
        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            return null;
        }

        if (room == null) {
            return null;
        }

        if (!room.getUsers().contains(user)) {
            throw new ChatException("room", "User not in room");
        }

        List<String> emails = new ArrayList<>();
        for (UserModel userModel : room.getUsers()) {
            emails.add(userModel.getEmail());
        }

        return emails;
    }

    public void readMessages(String email, String roomId) throws ChatException {

        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            return;
        }

        if (room == null) {
            return;
        }

        if (!room.getUsers().contains(user)) {
            throw new ChatException("room", "User not in room");

        }

//        List<Chat> unreadChats = room.getChats();
        List<Chat> unreadChats = roomRepository.getUnreadMessages(roomId, user.getUserId());

        for (Chat chat : unreadChats) {
            if (!chat.getReadBy().contains(user)) {
                chat.getReadBy().add(user);
            }
            else {
                System.out.println("Already read");
            }
        }
        chatRepository.saveAll(unreadChats);
    }

    public boolean emailInRoom(String email, String roomId) throws ChatException {
        UserModel user = userService.getUserByEmail(email);
        Room room = roomRepository.findRoomByRoomId(roomId);

        if (user == null) {
            throw new ChatException("user", "User not found");
        }

        if (room == null) {
            throw new ChatException("room", "Room not found");
        }

        return room.getUsers().contains(user);
    }

}
