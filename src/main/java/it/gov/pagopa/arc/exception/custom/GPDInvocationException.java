package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class GPDInvocationException extends RuntimeException{

    public GPDInvocationException(String message){
        super(message);
    }
}
