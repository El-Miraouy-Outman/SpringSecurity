package org.sid.securiteservice.controller;

import lombok.Data;
import org.sid.securiteservice.entities.Role;
import org.sid.securiteservice.entities.User;
import org.sid.securiteservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findall(){
        return  userService.listUser();
    }

    @PostMapping("/role")
    public Role Rolesaverole(@RequestBody  Role role){
        return  userService.addRole(role);
    }

    @PostMapping("/user")
    public User saveuser(@RequestBody  User user){
        return  userService.addUser(user);
    }
    @PostMapping("/addroletouser")
    public void addusertorole(@RequestBody  FormUser formUser){
          userService.addRoleToUser(formUser.getUsername(),formUser.getRolename());
    }

}
