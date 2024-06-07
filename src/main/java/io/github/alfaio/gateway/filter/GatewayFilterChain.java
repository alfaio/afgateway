package io.github.alfaio.gateway.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/7
 **/
public interface GatewayFilterChain {

    Mono<Void> filter(ServerWebExchange exchange);

}
