package io.github.alfaio.gateway.plugin;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * direct proxy plugin
 *
 * @author LinMF
 * @since 2024/6/6
 **/
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "direct";
    private final String prefix = GATEWAY_PREFIX + "/" + NAME + "/";
    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange) {
        System.out.println(" ===>>> [DirectPlugin] ...");
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("af.gw.version", "v1.0.0");

        if (backend == null || backend.isEmpty()) {
            return requestBody.flatMap(x -> exchange.getResponse().writeWith(Mono.just(x)).then()).then();

        }

        // 5. 通过webclient发送post请求
        WebClient client = WebClient.create(backend);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6.通过entity获取响应报文
        Mono<String> responseBody = entity.map(ResponseEntity::getBody);
        // 7. 组装响应报文
        return responseBody.flatMap(s -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(s.getBytes()))));

    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public String getName() {
        return NAME;
    }
}