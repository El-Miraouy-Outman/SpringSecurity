package org.sid.securiteservice.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestRestAPI {
    @GetMapping("/test")
    public Map<String,Object> test(Authentication authentication){

        return Map.of(
                "A1","OUTMAN",
                "B2","MIRAOUY",
                "authaurities",authentication.getAuthorities(),
                 "name",authentication.getName()

        );
    }
    @PostMapping("/saveData")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public Map<String,String> saveData(Authentication authentication,String data){
        return Map.of("DataSaved",data);
    }

    @GetMapping("/hello")
    public Map<String,String> hello(){

        return Map.of("hello","mam");
    }
}
