package org.sid.securiteservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailService
       // implements UserDetailsService
        {
    @Autowired
    private UserService userService;

            //@Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                org.sid.securiteservice.entities.User users=userService.loadUserByName(username);
                Collection<GrantedAuthority> grantedAuthorities=new ArrayList<>();
                users.getRoles().forEach(r->{
                    grantedAuthorities.add(new SimpleGrantedAuthority(r.getName()));
                });
                return new User(users.getName(),users.getPassword(),grantedAuthorities);
            }
        }
