package ru.uh635c.personservice.config;

import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class JsonbToStringConverter implements Converter<Json, String> {

    @Override
    public String convert(Json source) {
        // Retrieve the JSONB value from the DB as a String
        return source.asString();
    }
}
