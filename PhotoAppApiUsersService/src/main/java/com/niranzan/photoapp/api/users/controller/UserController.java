/**
 * 
 */
package com.niranzan.photoapp.api.users.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niranzan.photoapp.api.users.service.UsersService;
import com.niranzan.photoapp.api.users.shared.UserDto;
import com.niranzan.photoapp.api.users.ui.model.CreateUserModel;
import com.niranzan.photoapp.api.users.ui.model.CreateUserResponseModel;

/**
 * @author niranjanmaharana
 *
 */

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private Environment environment;
	@Autowired
	private UsersService service;

	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + environment.getProperty("local.server.port");
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserModel user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		UserDto userDto = mapper.map(user, UserDto.class);
		UserDto createdUserDto = service.createUser(userDto);
		CreateUserResponseModel responseModel = mapper.map(createdUserDto, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
	}
}
