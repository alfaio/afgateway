package io.github.alfaio.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * gateway post web filter
 *
 * @author LinMF
 * @since 2024/6/6
 **/
@Component
public class GatewayPostWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(s -> {
            System.out.println(" ===>>> AF Gateway post web filter");
            exchange.getAttributes().forEach((k, v) -> System.out.println(k + ":" + v));
        });
    }
}
