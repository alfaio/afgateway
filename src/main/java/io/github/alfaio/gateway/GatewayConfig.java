package io.github.alfaio.gateway;

import io.github.alfaio.afrpc.core.api.RegistryCenter;
import io.github.alfaio.afrpc.core.registry.af.AfRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
