package com.niranzan.photoapp.user.ws.service;

import com.niranzan.photoapp.user.ws.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserDetailsByEmail(String email);
}
