package ru.uh635c.personservice.config;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Javers javers(){
        return JaversBuilder.javers().build();
    }
}
