/**
 * 
 */
package com.niranzan.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niranzan.photoapp.api.users.service.UsersService;
import com.niranzan.photoapp.api.users.shared.UserDto;
import com.niranzan.photoapp.api.users.ui.model.LoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author niranjanmaharana
 *
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private UsersService usersService;
	private Environment environment;

	@Autowired
	public AuthenticationFilter(UsersService usersService, Environment environment, AuthenticationManager authenticationManager) {
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequestModel loginRequest = new ObjectMapper().readValue(request.getInputStream(),
					LoginRequestModel.class);
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
					loginRequest.getEmail(), loginRequest.getPassword(), new ArrayList<>()));
		} catch (IOException exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String username = ((User) auth.getPrincipal()).getUsername();
		UserDto userDto = usersService.getUserDetailsByEmail(username);
		Calendar expiration = Calendar.getInstance();
		expiration.setTimeInMillis(System.currentTimeMillis() + Long.parseLong(environment.getProperty("jwt.token.expire.time")));
		String token = Jwts.builder()
				.setSubject(userDto.getUserId())
				.setExpiration(expiration.getTime())
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.token.secret.key"))
				.compact();
		
		response.addHeader("token", token);
		response.addHeader("userId", userDto.getUserId());
	}
}
