package ru.uh635c.personservice.errorhandling;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import ru.uh635c.personservice.entity.dtos.ErrorDto;
import ru.uh635c.personservice.exceptions.CustomException;

import java.util.Map;

@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest webRequest, ErrorAttributeOptions options) {
        Throwable error = getError(webRequest);
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        if(error instanceof CustomException) {
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
            errorAttributes.put("body", ErrorDto.builder()
                    .errorCode(((CustomException)error).getErrorCode())
                    .errorMessage(((CustomException)error).getMessage())
                    .build());
        }else{
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            errorAttributes.put("message", ErrorDto.builder()
                    .errorCode("INTERNAL_SERVER_ERROR")
                    .errorMessage(error.getMessage()));
        }

        return errorAttributes;
    }
}
