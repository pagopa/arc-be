package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class PullPaymentInvalidRequestException extends RuntimeException{
    public PullPaymentInvalidRequestException(String message){super(message);}
}
