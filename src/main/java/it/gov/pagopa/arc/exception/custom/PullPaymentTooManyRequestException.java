package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class PullPaymentTooManyRequestException extends RuntimeException{
    public PullPaymentTooManyRequestException(String message){
        super(message);
    }
}
