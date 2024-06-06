package io.github.alfaio.gateway.plugin;

import io.github.alfaio.afrpc.core.api.LoadBalancer;
import io.github.alfaio.afrpc.core.api.RegistryCenter;
import io.github.alfaio.afrpc.core.cluster.RoundRibonLoadBalancer;
import io.github.alfaio.afrpc.core.meta.InstanceMeta;
import io.github.alfaio.afrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author LinMF
 * @since 2024/6/6
 **/
@Component("afrpc")
public class AfRpcPlugin extends  AbstractGatewayPlugin{
    public static final String NAME = "afrpc";
    private final String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    RegistryCenter rc;
    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer<>();

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange) {
        System.out.println(" ===>>> [AfrpcPlugin] ...");
        // 1. 通过请求路径获取服务名
        String service = exchange.getRequest().getPath().value().substring(prefix.length());
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有服务实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        // 3. 使用负载均衡，拿到实例的url
        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        String url = instanceMeta.toUrl();
        // 4. 拿到请求报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        // 5. 通过webclient发送post请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6.通过entity获取响应报文
        Mono<String> responseBody = entity.map(ResponseEntity::getBody);
        // 7. 组装响应报文
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("af.gw.version", "v1.0.0");
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
