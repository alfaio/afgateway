package io.github.alfaio.gateway.filter;

import io.github.alfaio.gateway.plugin.GatewayPluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/7
 **/
public interface GatewayFilter {

    Mono<Void> handler(ServerWebExchange exchange, GatewayFilterChain chain);
}
