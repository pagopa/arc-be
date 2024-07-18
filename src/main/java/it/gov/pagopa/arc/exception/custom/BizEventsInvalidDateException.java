package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsInvalidDateException extends RuntimeException{
    public BizEventsInvalidDateException(String message){super(message);}
}
