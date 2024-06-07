package io.github.alfaio.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/6/7
 **/
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(" ===>>> filters: demo filter ...");
        exchange.getRequest().getHeaders().forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        return chain.filter(exchange);
    }
}
