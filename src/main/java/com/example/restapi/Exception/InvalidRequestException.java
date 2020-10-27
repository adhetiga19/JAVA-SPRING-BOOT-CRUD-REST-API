package com.example.restapi.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidRequestException extends Exception{
    public InvalidRequestException(String message) {
        super(message);
    }
}
