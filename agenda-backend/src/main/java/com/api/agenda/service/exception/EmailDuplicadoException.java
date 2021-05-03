package com.api.agenda.service.exception;

public class EmailDuplicadoException extends RuntimeException{

    public EmailDuplicadoException(String message) {
        super(message);
    }

    public EmailDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
