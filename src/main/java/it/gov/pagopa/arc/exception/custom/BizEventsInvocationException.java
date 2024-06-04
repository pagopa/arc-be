package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsInvocationException extends RuntimeException{
    public BizEventsInvocationException(String message){super(message);}
}
