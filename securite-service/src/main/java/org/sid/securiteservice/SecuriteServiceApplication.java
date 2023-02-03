package org.sid.securiteservice;

import org.sid.securiteservice.Configuration.RsakeysConfig;
import org.sid.securiteservice.entities.Role;
import org.sid.securiteservice.entities.User;
import org.sid.securiteservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableConfigurationProperties(RsakeysConfig.class)
public class SecuriteServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecuriteServiceApplication.class, args);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner start(UserService userService){
        return  args -> {
                 userService.addRole( new Role(null,"USER"));
            userService.addRole( new Role(null,"ADMIN"));
            userService.addRole( new Role(null,"VENDEUR"));
            userService.addRole( new Role(null,"ACHETEUR"));

            userService.addUser(new User(null,"user1","1234",new ArrayList<>()));
            userService.addUser(new User(null,"user2","1234",new ArrayList<>()));
            userService.addUser(new User(null,"user3","1234",new ArrayList<>()));
            userService.addUser(new User(null,"admin","1234",new ArrayList<>()));
            userService.addUser(new User(null,"vendeur","1234",new ArrayList<>()));

            userService.addRoleToUser("user1","USER");
            userService.addRoleToUser("user1","ADMIN");
            userService.addRoleToUser("admin","ADMIN");
            userService.addRoleToUser("user3","ACHETEUR");
          //  userService.addRoleToUser("vendeur","USER");
        } ;
    }

}

