package be.ucll.se.team15backend.manage.controller;

import be.ucll.se.team15backend.manage.model.ManageException;
import be.ucll.se.team15backend.manage.service.ManageService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;

    @Autowired
    private JwtService jwtService;

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/role/update")
    public UserModel updateRole(@RequestParam Long userId, @RequestParam String role) throws UserException, ManageException {
        return manageService.updateRole(userId, role);
    }

}
