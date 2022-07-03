/**
 * 
 */
package com.niranzan.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.niranzan.photoapp.api.users.shared.UserDto;

/**
 * @author niranjanmaharana
 *
 */

public interface UsersService extends UserDetailsService {
	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
}
