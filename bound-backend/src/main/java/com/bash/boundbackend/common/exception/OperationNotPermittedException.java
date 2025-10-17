package com.bash.boundbackend.common.exception;

public class OperationNotPermittedException extends RuntimeException{

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
