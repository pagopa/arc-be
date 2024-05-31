package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException{
    public InternalServerException(String message){super(message);}
}
