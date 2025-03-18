package ru.uh635c.personservice.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
@RequiredArgsConstructor
public class JsonbToStringConverter implements Converter<Json, JsonNode> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public JsonNode convert(Json source) {

        // Retrieve the JSONB value from the DB as a String
            return objectMapper.readTree(source.asString());
    }
}
