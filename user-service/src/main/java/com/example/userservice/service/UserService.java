package com.example.userservice.service;

import com.example.userservice.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUserDetailsByEmail(String email);
}
