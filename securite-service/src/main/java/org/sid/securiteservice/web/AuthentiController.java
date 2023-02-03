package org.sid.securiteservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Authentification Basic

@RestController
public class AuthentiController {
    @Autowired
    private JwtEncoder jwtEncoder;
    public AuthentiController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @PostMapping("/token")
  public Map<String,String> jwtToken(Authentication authentication){
        Map<String,String> idtoken=new HashMap<>();
       // token personnaolisee
        Instant instant=Instant.now();
       String scope=authentication.getAuthorities()
               .stream()
               .map(aut->aut.getAuthority() )
               .collect(Collectors.joining(" "));
        JwtClaimsSet jwtClaimsSet= JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
                .issuer("securite-service")
                .claim("scope",scope)
                .build();

        String jwtAccestoken =jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        idtoken.put("accesstoken",jwtAccestoken);
        return idtoken;
    }

}
