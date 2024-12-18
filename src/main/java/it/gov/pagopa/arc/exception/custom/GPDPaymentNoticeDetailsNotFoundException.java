package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class GPDPaymentNoticeDetailsNotFoundException extends RuntimeException{
    public GPDPaymentNoticeDetailsNotFoundException(String message){
        super(message);
    }
}
