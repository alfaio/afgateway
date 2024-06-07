package io.github.alfaio.gateway.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author LinMF
 * @since 2024/6/7
 **/
public class DefaultGatewayFilterChain implements GatewayFilterChain{

    private final List<GatewayFilter> filters;
    private final int index;

    public DefaultGatewayFilterChain(List<GatewayFilter> filters) {
        this.filters = filters;
        index = 0;
    }

    public DefaultGatewayFilterChain(List<GatewayFilter> filters, int index) {
        this.filters = filters;
        this.index = index;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        if (filters == null || filters.isEmpty()) {
            return Mono.empty();
        }
        if (index < filters.size()) {
            GatewayFilter filter = filters.get(index);
            return filter.filter(exchange, new DefaultGatewayFilterChain(filters, index + 1));
        }
        return Mono.empty();
    }
}
