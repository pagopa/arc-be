package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class GPDTooManyRequestException extends RuntimeException{
    public GPDTooManyRequestException(String message){super(message);}
}
