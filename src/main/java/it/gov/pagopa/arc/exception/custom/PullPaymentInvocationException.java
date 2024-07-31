package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class PullPaymentInvocationException extends RuntimeException{

    public PullPaymentInvocationException(String message){super(message);}
}
