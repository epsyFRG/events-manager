package com.eventmanager.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("ci sono stati errori di validazione");
        this.errorsMessages = errorsMessages;
    }
}