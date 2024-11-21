package be.ucll.se.team15backend.user.service;

import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel addUser(UserModel user) throws UserException {
        if (userRepository.findByEmail(user.getEmail()).orElse(null) != null) {
            throw new UserException("email", "Email already in use");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    public UserModel updateUser(UserModel user) {
        return userRepository.save(user);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserModel getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserModel getBot() {
        return userRepository.findByEmail("boxter.buddy@ucll.be").orElse(null);
    }

    public String getUserEmailById(Long userId) {
        UserModel user = userRepository.findById(userId).orElse(null);
        return (user != null) ? user.getEmail() : null;
    }

    public List<UserModel> getAllUsersExceptBot() {
        return userRepository.findAll().stream().filter(userModel -> userModel.getRole() != Role.BOT).toList();
    }

    public List<UserModel> getAllAdmins() {
        return userRepository.findAll().stream().filter(userModel -> userModel.getRole() == Role.ADMIN).toList();
    }
}
