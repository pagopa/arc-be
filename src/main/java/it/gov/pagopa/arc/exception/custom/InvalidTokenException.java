package it.gov.pagopa.arc.exception.custom;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }
}