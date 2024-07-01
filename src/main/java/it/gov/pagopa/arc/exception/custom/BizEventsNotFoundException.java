package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsNotFoundException extends RuntimeException {
    public BizEventsNotFoundException(String message){super(message);}
}
