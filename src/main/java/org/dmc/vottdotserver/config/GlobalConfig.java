package org.dmc.vottdotserver.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    /**
     * The Model Mapper allows easy mapping between DTOs and domain objects.
     *
     * @return the model mapper bean.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
