package io.github.alfaio.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/7
 **/
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
