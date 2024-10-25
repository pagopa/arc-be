package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsTransactionNotFoundException extends RuntimeException {
    public BizEventsTransactionNotFoundException(String message){super(message);}
}
