package com.joshuapavan.jwtauthentication.services;

import com.joshuapavan.jwtauthentication.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();

    User createUser(User user);

    UserDetailsService userDetailService();
}
