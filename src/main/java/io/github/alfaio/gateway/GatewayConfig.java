package io.github.alfaio.gateway;

import io.github.alfaio.afrpc.core.api.RegistryCenter;
import io.github.alfaio.afrpc.core.registry.af.AfRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @author LinMF
 * @since 2024/5/23
 **/
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        return new AfRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put("/ga/*", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("af gateway start");
        };
    }
}
