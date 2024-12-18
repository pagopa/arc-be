package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class GPDInvalidRequestException  extends RuntimeException{
    public GPDInvalidRequestException(String message){
        super(message);
    }
}
