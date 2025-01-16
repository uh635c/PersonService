package ru.uh635c.personservice.exceptions;

public class UserSavingException extends CustomException{
    public UserSavingException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
