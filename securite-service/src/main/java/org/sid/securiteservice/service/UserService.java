package org.sid.securiteservice.service;

import org.sid.securiteservice.entities.Role;
import org.sid.securiteservice.entities.User;

import java.util.List;


public interface UserService {
    User addUser(User USER);
    Role addRole(Role role);
    void addRoleToUser(String nameOfuser,String Rolename);
    User loadUserByName(String name);
    List<User> listUser();
}
