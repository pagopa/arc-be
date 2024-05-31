package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class TooManyRequestException extends RuntimeException{
    public TooManyRequestException(String message){super(message);}
}
