/**
 * 
 */
package com.niranzan.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.niranzan.photoapp.api.users.data.UserEntity;
import com.niranzan.photoapp.api.users.data.UsersRepository;
import com.niranzan.photoapp.api.users.shared.UserDto;

/**
 * @author niranjanmaharana
 *
 */

@Service
public class UsersServiceImpl implements UsersService {
	@Autowired
	private UsersRepository repository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity entity = repository.findByEmail(username);
		if (entity == null) throw new UsernameNotFoundException("username");
		return new User(entity.getEmail(), 
				entity.getEncryptedPassword(), 
				true, 
				true, 
				true, 
				true, 
				new ArrayList<>());
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		UserEntity user = modelMapper.map(userDetails, UserEntity.class);
		user.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
		UserEntity entity = repository.save(user);
		return modelMapper.map(entity, UserDto.class);
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity entity = repository.findByEmail(email);
		if (entity == null) throw new UsernameNotFoundException("username");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return modelMapper.map(entity, UserDto.class);
	}
}
