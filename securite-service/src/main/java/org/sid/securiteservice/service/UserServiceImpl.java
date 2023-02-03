package org.sid.securiteservice.service;

import org.sid.securiteservice.Repository.RoleRepository;
import org.sid.securiteservice.Repository.UserRepository;
import org.sid.securiteservice.entities.Role;
import org.sid.securiteservice.entities.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addUser(User USER) {
        String ps =USER.getName();
        USER.setPassword(passwordEncoder.encode(ps));
        return userRepository.save(USER);
    }

    @Override
    public void addRoleToUser(String nameOfuser, String Rolename) {
      User user=userRepository.findByName(nameOfuser);
      Role role=roleRepository.findByName(Rolename);
      user.getRoles().add(role);
    }

    @Override
    public User loadUserByName(String name) {

        return userRepository.findByName(name) ;
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }
}
