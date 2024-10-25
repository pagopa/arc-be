package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsPaidNoticeNotFoundException extends RuntimeException {
    public BizEventsPaidNoticeNotFoundException(String message){super(message);}
}
