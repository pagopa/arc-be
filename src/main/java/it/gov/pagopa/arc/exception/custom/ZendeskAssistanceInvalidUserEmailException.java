package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class ZendeskAssistanceInvalidUserEmailException extends RuntimeException{

    public ZendeskAssistanceInvalidUserEmailException(String message) {
        super(message);
    }
}
