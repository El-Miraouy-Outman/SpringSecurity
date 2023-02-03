package org.sid.securiteservice.Configuration;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
//import jakarta.servlet.FilterChain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig   {
    private PasswordEncoder passwordEncoder;
    public SecurityConfig(PasswordEncoder passwordEncoder, RsakeysConfig rsakeysConfig) {
        this.passwordEncoder = passwordEncoder;
        this.rsakeysConfig = rsakeysConfig;
    }

    private RsakeysConfig rsakeysConfig;


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        var authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return  new ProviderManager(authenticationProvider);
    }

 @Bean
    public UserDetailsService inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1111")).authorities("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("1111")).authorities("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1111")).authorities("ADMIN").build()
                );
    }


    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/tokenP","/hello").permitAll())
                .sessionManagement(x->x.sessionCreationPolicy((SessionCreationPolicy.STATELESS) ))
                .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    // nous besoi deux clee prive et public il faut signer les token avec 'eresa' pour le gererr openSSL OU BIEN JAVA
    // ligne de commande ans resource cre doucier sertificat
    //1 cmd openssl genrsa -out keypair.pim 2048
    //2 eme public key> openssl rsa -in keypair.pem -pubout -out public.pem
    //3 eme private key sous forme pk> openssl pkcs8 -topk8 -inform PEM -nocrypt -in keypair.pem -out private.pem
  @Bean
   public JwtEncoder jwtEncoder(){
       JWK jwk=new RSAKey.Builder(rsakeysConfig.publickey())
                         .privateKey(rsakeysConfig.privatekey())
                          .build();
       JWKSource<SecurityContext> jwkSource=new ImmutableJWKSet<>(new JWKSet(jwk));
       return new NimbusJwtEncoder(jwkSource);
   }
   @Bean
   public JwtDecoder jwtDecoder(){
    return NimbusJwtDecoder
            .withPublicKey(rsakeysConfig.publickey())
            .build();
   }
}
