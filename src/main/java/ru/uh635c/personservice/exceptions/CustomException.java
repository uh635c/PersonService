package ru.uh635c.personservice.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    protected String errorCode;

    public CustomException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
