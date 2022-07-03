/**
 * 
 */
package com.niranzan.photoapi.api.gateway.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author niranjanmaharana
 * @param <T>
 *
 */

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	
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
				authorizationHeader.replace("Bearer", "");
			}
			
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus unauthorized) {
		ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();
		response.setStatusCode(unauthorized);
		return response.setComplete();
	}
}
