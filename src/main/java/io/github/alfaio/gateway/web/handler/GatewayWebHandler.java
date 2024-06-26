package io.github.alfaio.gateway.web.handler;

import io.github.alfaio.gateway.filter.DefaultGatewayFilterChain;
import io.github.alfaio.gateway.filter.GatewayFilter;
import io.github.alfaio.gateway.plugin.DefaultGatewayPluginChain;
import io.github.alfaio.gateway.plugin.GatewayPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author LinMF
 * @since 2024/5/29
 **/
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;
    @Autowired
    List<GatewayFilter> filters;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ===>>> AF Gateway we handler ...");
        if (plugins == null || plugins.isEmpty()) {
            String mock = """
                    {"result":"no plugin"}
                    """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }
        new DefaultGatewayFilterChain(filters).filter(exchange);
        return new DefaultGatewayPluginChain(plugins).handle(exchange);
    }
}
