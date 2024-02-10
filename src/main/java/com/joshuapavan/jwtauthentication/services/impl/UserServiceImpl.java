package com.joshuapavan.jwtauthentication.services.impl;

import com.joshuapavan.jwtauthentication.entities.User;
import com.joshuapavan.jwtauthentication.repositories.UserRepository;
import com.joshuapavan.jwtauthentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }


    @Override
    public UserDetailsService userDetailService() {
         return new UserDetailsService() {
             @Override
             public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                         .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email : %s, was not found", username)));
             }
         };
    }
}
