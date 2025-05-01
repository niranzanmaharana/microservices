package com.niranzan.photoapp.api.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

// @Configuration
public class GlobalFiltersConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            LOGGER.info("Second global pre filter executed");
            LOGGER.info("\n\n");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.info("Third global post filter executed");
                LOGGER.info("\n\n");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            LOGGER.info("Third global pre filter executed");
            LOGGER.info("\n\n");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.info("Second global post filter executed");
                LOGGER.info("\n\n");
            }));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter fourthPreFilter() {
        return (exchange, chain) -> {
            LOGGER.info("Fourth global pre filter executed");
            LOGGER.info("\n\n");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.info("\n\n");
                LOGGER.info("First global post filter executed");
            }));
        };
    }
}
