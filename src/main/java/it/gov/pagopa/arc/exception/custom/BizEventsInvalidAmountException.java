package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsInvalidAmountException extends RuntimeException{
    public BizEventsInvalidAmountException(String message){super(message);}
}
