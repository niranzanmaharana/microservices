package com.niranzan.photoapp.user.ws.service;

import com.niranzan.photoapp.user.ws.client.AlbumServiceClient;
import com.niranzan.photoapp.user.ws.entity.UserEntity;
import com.niranzan.photoapp.user.ws.exceptions.UserServiceException;
import com.niranzan.photoapp.user.ws.model.AlbumResponseModel;
import com.niranzan.photoapp.user.ws.model.UserDto;
import com.niranzan.photoapp.user.ws.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AlbumServiceClient albumServiceClient;

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

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException("User not found with user id: " + userId);
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        try {
            List<AlbumResponseModel> albums = albumServiceClient.getAlbums(userId);
            userDto.setAlbums(albums);
        } catch (FeignException.FeignClientException exception) {
            log.error("Exception while reading the albums from album-ws: {}", exception.getMessage());
        }
        return userDto;
    }
}
