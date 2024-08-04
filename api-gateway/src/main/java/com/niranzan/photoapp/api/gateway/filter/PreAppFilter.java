package com.niranzan.photoapp.api.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PreAppFilter implements GlobalFilter, Ordered {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOGGER.info("First pre global filter is executed");
        String requestUriPath = exchange.getRequest().getPath().toString();
        LOGGER.info("Request Path: {}", requestUriPath);
        HttpHeaders headers = exchange.getRequest().getHeaders();
        headers.keySet().forEach(headerName -> LOGGER.info("Header Name: {}, Value: {}", headerName, headers.getFirst(headerName)));
        LOGGER.info("\n\n");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
