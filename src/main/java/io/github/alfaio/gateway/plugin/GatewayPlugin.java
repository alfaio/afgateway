package io.github.alfaio.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/6
 **/
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    void start();
    void stop();

    String getName();

    boolean support(ServerWebExchange exchange);

    Mono<Void> handler(ServerWebExchange exchange);

}
