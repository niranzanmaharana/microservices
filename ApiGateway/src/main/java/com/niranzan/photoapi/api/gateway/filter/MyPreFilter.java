/**
 * 
 */
package com.niranzan.photoapi.api.gateway.filter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author niranjanmaharana
 *
 */

@Component
public class MyPreFilter implements GlobalFilter, Ordered {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPreFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		LOGGER.info("My first pre filter is executed");
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
		LOGGER.info("Request URI Path :: {}", request.getPath());
		Set<String> headerNames = request.getHeaders().keySet();
		headerNames.forEach(name -> LOGGER.info("{} :: {}", name, request.getHeaders().getFirst(name)));
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}
	
}
