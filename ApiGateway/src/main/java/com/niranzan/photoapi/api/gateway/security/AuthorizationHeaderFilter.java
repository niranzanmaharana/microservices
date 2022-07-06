/**
 * 
 */
package com.niranzan.photoapi.api.gateway.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import reactor.core.publisher.Mono;

/**
 * @author niranjanmaharana
 * @param <T>
 *
 */

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	@Autowired
	private Environment environment;
	
	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
	public static class Config {
		
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization header available!", HttpStatus.UNAUTHORIZED);
			}
			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer")) {
				try {
					if (!isValidJwtToken(authorizationHeader.replace("Bearer", ""))) {
						return onError(exchange, "Invalid jwt token!", HttpStatus.UNAUTHORIZED);
					}
				} catch (MalformedJwtException exception) {
					return onError(exchange, "Invalid jwt token!", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return onError(exchange, "Invalid jwt token!", HttpStatus.UNAUTHORIZED);
			}
			
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus unauthorized) {
		ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();
		response.setStatusCode(unauthorized);
		return response.setComplete();
	}
	
	private boolean isValidJwtToken(String token) {
		String userId = Jwts.parser().setSigningKey(environment.getProperty("jwt.token.secret.key"))
				.parseClaimsJws(token).getBody().getSubject();
		return StringUtils.isNotBlank(userId);
	}
}
