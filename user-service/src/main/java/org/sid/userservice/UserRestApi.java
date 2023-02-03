package org.sid.userservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestApi {
    @GetMapping("/usersservice")
    //@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public Map<String,Object> user(Authentication authentication){
        return Map.of(
                "name","outman",
                "email","elmiraouy@gmail.com",
                "username",authentication.getName(),
               "scope",authentication.getAuthorities()
                );
    }
}
