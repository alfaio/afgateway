package io.github.alfaio.gateway;

import io.github.alfaio.afrpc.core.api.LoadBalancer;
import io.github.alfaio.afrpc.core.api.RegistryCenter;
import io.github.alfaio.afrpc.core.cluster.RoundRibonLoadBalancer;
import io.github.alfaio.afrpc.core.meta.InstanceMeta;
import io.github.alfaio.afrpc.core.meta.ServiceMeta;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author LinMF
 * @since 2024/5/23
 **/
@Component
public class GatewayHandler {


    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer<>();

    Mono<ServerResponse> handle(ServerRequest request) {
        // 1. 通过请求路径获取服务名
        String service = request.path().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有服务实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        // 3. 使用负载均衡，拿到实例的url
        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        String url = instanceMeta.toUrl();
        // 4. 拿到请求报文
        Mono<String> requestMono = request.bodyToMono(String.class);
        return requestMono.flatMap(x -> invokeFromRegistry(x, url));

    }

    @NotNull
    private static Mono<ServerResponse> invokeFromRegistry(String x, String url) {
        // 5. 通过webclient发送post请求
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .bodyValue(x).retrieve().toEntity(String.class);
        // 6.通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.subscribe(source -> System.out.println("Response: " + source));
        // 7. 组装响应报文
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("af.gw.version", "v1.0.0")
                .body(body, String.class);
    }
}
