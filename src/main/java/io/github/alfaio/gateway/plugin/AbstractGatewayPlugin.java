package io.github.alfaio.gateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/6
 **/
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    @Override
    public void start() {}

    @Override
    public void stop() {}


    @Override
    public Mono<Void> handler(ServerWebExchange exchange, GatewayPluginChain chain) {
        boolean support = support(exchange);
        System.out.println(" ===>>> plugin [" + this.getName() + "], support = " + support);
        return support ? doHandle(exchange, chain) : chain.handle(exchange);
    }

    public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain);

}
