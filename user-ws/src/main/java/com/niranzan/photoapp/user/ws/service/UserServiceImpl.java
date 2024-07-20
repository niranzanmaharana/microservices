package com.niranzan.photoapp.user.ws.service;

import com.niranzan.photoapp.user.ws.entity.UserEntity;
import com.niranzan.photoapp.user.ws.model.UserDto;
import com.niranzan.photoapp.user.ws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUserDto = userRepository.save(userEntity);
        return modelMapper.map(savedUserDto, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new ModelMapper().map(userEntity, UserDto.class);
    }
}
