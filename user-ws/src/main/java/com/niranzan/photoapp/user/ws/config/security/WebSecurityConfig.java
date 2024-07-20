package com.niranzan.photoapp.user.ws.config.security;

import com.niranzan.photoapp.user.ws.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment environment;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = getAuthenticationManager(http);

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(requests ->
                        requests.requestMatchers("/users/**").permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .anyRequest().authenticated()
                ).addFilterBefore(new AuthenticationFilter(userService, environment, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager);

        http.sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    private AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }
}
