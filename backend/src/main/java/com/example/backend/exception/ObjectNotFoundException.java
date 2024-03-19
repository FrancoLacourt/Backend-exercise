package com.example.backend.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(){
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

}
