package be.ucll.se.team15backend.manage.service;

import be.ucll.se.team15backend.manage.model.ManageException;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageService {

    @Autowired
    private UserService userService;
    public UserModel updateRole(Long userId, String role) throws ManageException, UserException {
        UserModel user = userService.getUserById(userId);
        if (user == null) {
            throw new ManageException("userId", "User not found");
        }
        Role newRole;
        try {
            newRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManageException("role", "Role not found");
        }

        user.setRole(newRole);
        return userService.updateUser(user);

    }

}
