package it.gov.pagopa.arc.exception;

import it.gov.pagopa.arc.model.generated.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingRequestHeaderExceptions(
            MissingRequestHeaderException ex, HttpServletRequest request) {

        String message = ex.getMessage();

        log.info("A MissingRequestHeaderException occurred handling request {}: HttpStatus 400 - {}",
                ExceptionHandler.getRequestDetails(request), message);
        log.debug("Something went wrong handling request", ex);
        return new ErrorDTO(ErrorDTO.ErrorEnum.INVALID_REQUEST, message);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String message = ex.getMessage();

        log.info("A MissingServletRequestParameterException occurred handling request {}: HttpStatus 400 - {}",
                ExceptionHandler.getRequestDetails(request), message);
        log.debug("Something went wrong handling request", ex);
        return new ErrorDTO(ErrorDTO.ErrorEnum.INVALID_REQUEST, message);
    }

    private static String getRequestDetails(HttpServletRequest request) {
        return "%s %s".formatted(request.getMethod(), request.getRequestURI());
    }
}
