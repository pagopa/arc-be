package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class BizEventsTooManyRequestException extends RuntimeException {
    public BizEventsTooManyRequestException(String message){
        super(message);
    }
}
