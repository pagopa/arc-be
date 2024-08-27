package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsReceiptNotFoundException extends RuntimeException {
    public BizEventsReceiptNotFoundException(String message){super(message);}
}
