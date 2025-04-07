package ru.uh635c.personservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Config extends AbstractR2dbcConfiguration {

    private final ObjectMapper objectMapper;

//    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        return null;
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(new StringToJsonbConverter());
        converters.add(new JsonbToStringConverter(objectMapper));
        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }

    @Bean
    public Javers javers(){
        return JaversBuilder.javers().build();
    }
}
