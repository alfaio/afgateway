package io.github.alfaio.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author LinMF
 * @since 2024/5/23
 **/
@Component
public class HelloHandler {

    Mono<ServerResponse> handle(ServerRequest request) {

        String url = "http://localhost:8081/afrpc";
        String requestJson = """
                {
                    "service":"io.github.alfaio.afrpc.demo.api.UserService",
                    "methodSign":"findById@1_int",
                    "args":[100]
                }
                """;
        WebClient client  = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post().header("Content-Type", "application/json")
                .bodyValue(requestJson).retrieve().toEntity(String.class);


        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("af.gw.version", "v1.0.0")
                .body(entity.map(ResponseEntity::getBody), String.class);
    }
}
