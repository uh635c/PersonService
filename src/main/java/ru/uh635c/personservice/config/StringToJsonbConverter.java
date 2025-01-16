package ru.uh635c.personservice.config;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StringToJsonbConverter implements Converter<String, Json> {
    @Override
    public Json convert(String source) {
        // Ensure valid JSON, or you can manipulate/validate if needed
        return Json.of(source);
    }
}
