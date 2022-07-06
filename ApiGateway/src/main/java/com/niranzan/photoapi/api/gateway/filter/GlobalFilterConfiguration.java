/**
 * 
 */
package com.niranzan.photoapi.api.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

/**
 * @author niranjanmaharana
 *
 */

@Configuration
public class GlobalFilterConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

	@Order(1)
	@Bean
    GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            LOGGER.info("[GlobalFilterConfiguration] Second Global Pre Filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.info("[GlobalFilterConfiguration] Second Global Post Filter executed");
            }));
        };
    }

	@Order(2)
    @Bean
    GlobalFilter thridPreFilter() {
        return (exchange, chain) -> {
            LOGGER.info("[GlobalFilterConfiguration] Third Global Pre Filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.info("[GlobalFilterConfiguration] Third Global Post Filter executed");
            }));
        };
    }
}
