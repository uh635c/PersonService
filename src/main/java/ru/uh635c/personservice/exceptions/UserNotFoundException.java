package ru.uh635c.personservice.exceptions;

public class UserNotFoundException extends CustomException{

    public UserNotFoundException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
