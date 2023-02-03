package org.sid.securiteservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Authentification personnaliser

@RestController
public class AuthentificationPersonnalise {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager  ;
    public AuthentificationPersonnalise(JwtEncoder jwtEncoder,AuthenticationManager authenticationManager,
                                         UserDetailsService userDetailsService) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService=userDetailsService;
    }
    @PostMapping("/tokenP")
    public ResponseEntity<Map<String,String> >jwtToken(String grantType,
                                                       String name,String password
                                                       ,boolean withrefrechtoken,
                                                        String refrechtoken){
        String subject=null;
        String scope=null;
        if(grantType.equals("password")) {
            Authentication  authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(name, password));
            subject=authentication.getName();
            scope=authentication.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.joining(""));
        } else if (grantType.equals("refrechtoken")) {
            if(refrechtoken==null) {
                return  new ResponseEntity<>(Map.of("ErrorMesaage","RefrecheToken Is Required"), HttpStatus.UNAUTHORIZED);
            }

            Jwt decodeJwt= null;
            try {
                decodeJwt = jwtDecoder.decode(refrechtoken);
            } catch (JwtException e) {
                return  new ResponseEntity<>(Map.of("ErrorMesaage",e.getMessage()), HttpStatus.UNAUTHORIZED);
            }
            System.out.println("code-------"+decodeJwt);
            subject=decodeJwt.getSubject();
            UserDetails userDetails= userDetailsService.loadUserByUsername(subject);
            System.out.println("user----"+subject);
            Collection<? extends GrantedAuthority> authorities=userDetails.getAuthorities();
            scope=authorities.stream().map(auth->auth.getAuthority()).collect(Collectors.joining(""));
        }
        Map<String,String> tokenPersonnaliser=new HashMap<>();
        Instant instant=Instant.now();
        JwtClaimsSet jwtClaimsSet= JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(withrefrechtoken?3:5, ChronoUnit.MINUTES))
                .issuer("securite-service")
                .claim("scope",scope)
                .build();

        String jwtAccestoken =jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        tokenPersonnaliser.put("accesstoken",jwtAccestoken);
        if(withrefrechtoken){
            JwtClaimsSet jwtClaimsSetref= JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .issuer("securite-service")
                    .build();
            String jwtRefrechtoken =jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
            tokenPersonnaliser.put("refrechToken",jwtRefrechtoken);
        }
        return new  ResponseEntity<>(tokenPersonnaliser,HttpStatus.OK);
    }

}

