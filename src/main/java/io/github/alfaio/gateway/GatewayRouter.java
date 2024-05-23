package io.github.alfaio.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author LimMF
 * @since 2024/5/23
 **/
@Component
public class GatewayRouter {

    @Autowired
    HelloHandler helloHandler;
    @Autowired
    GatewayHandler gatewayHandler;

    @Bean
    public RouterFunction<?> userRouterFunction() {
        return route(GET("/hello"), helloHandler::handle);
    }

    @Bean
    public RouterFunction<?> gwRouterFunction() {
        return route(GET("/gw").or(POST("/gw/**")), gatewayHandler::handle);
    }

}
