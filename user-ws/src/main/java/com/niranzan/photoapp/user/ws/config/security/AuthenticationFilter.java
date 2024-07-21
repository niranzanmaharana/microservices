package com.niranzan.photoapp.user.ws.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niranzan.photoapp.user.ws.model.LoginRequestModel;
import com.niranzan.photoapp.user.ws.model.UserDto;
import com.niranzan.photoapp.user.ws.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment environment;
    /*@Value("${jwtExpiration}")
    private long jwtExpiration;
    @Value("${jwtSecret}")
    private String jwtSecret;*/

    public AuthenticationFilter(UserService userService, Environment environment, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestModel loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            new ArrayList<>()
                    ));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) {
        String username = ((User) auth.getPrincipal()).getUsername();
        UserDto user = userService.getUserDetailsByEmail(username);

        Instant now = Instant.now();
        byte[] secretKeyBytes = Base64.getEncoder().encode(Objects.requireNonNull(environment.getProperty("jwt.secret")).getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setExpiration(Date.from(now.plusSeconds(Long.parseLong(Objects.requireNonNull(environment.getProperty("jwt.expiration"))))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", user.getUserId());
    }
}
