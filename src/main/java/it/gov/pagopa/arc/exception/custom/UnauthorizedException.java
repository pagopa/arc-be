package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){super(message);}
}
