package com.niranzan.photoapp.user.ws.controller;

import com.niranzan.photoapp.user.ws.model.CreateUserRequestModel;
import com.niranzan.photoapp.user.ws.model.CreateUserResponseModel;
import com.niranzan.photoapp.user.ws.model.UserDto;
import com.niranzan.photoapp.user.ws.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private Environment environment;
    @Autowired
    private UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port: " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto savedUserDto = userService.createUser(userDto);
        CreateUserResponseModel responseModel = modelMapper.map(savedUserDto, CreateUserResponseModel.class);
        return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
    }
}
