package it.gov.pagopa.arc.exception;

import it.gov.pagopa.arc.dto.ArcErrorDTO;
import it.gov.pagopa.arc.exception.custom.InternalServerException;
import it.gov.pagopa.arc.exception.custom.TooManyRequestException;
import it.gov.pagopa.arc.exception.custom.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ArcExceptionHandler {
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ArcErrorDTO handleMissingRequestHeaderExceptions(
            MissingRequestHeaderException ex, HttpServletRequest request) {

        String message = ex.getMessage();

        log.info("A MissingRequestHeaderException occurred handling request {}: HttpStatus 400 - {}",
                ArcExceptionHandler.getRequestDetails(request), message);
        log.debug("Something went wrong handling request", ex);
        return new ArcErrorDTO(ArcErrorDTO.ErrorEnum.INVALID_REQUEST, message);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ArcErrorDTO handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String message = ex.getMessage();

        log.info("A MissingServletRequestParameterException occurred handling request {}: HttpStatus 400 - {}",
                ArcExceptionHandler.getRequestDetails(request), message);
        log.debug("Something went wrong handling request", ex);
        return new ArcErrorDTO(ArcErrorDTO.ErrorEnum.INVALID_REQUEST, message);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ArcErrorDTO> handleUnauthorizedException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.UNAUTHORIZED, ArcErrorDTO.ErrorEnum.UNAUTHORIZED);
    }
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ArcErrorDTO> handleTooManyRequestException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.TOO_MANY_REQUESTS, ArcErrorDTO.ErrorEnum.TOO_MANY_REQUEST);
    }
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ArcErrorDTO> handleInternalServerException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ArcErrorDTO.ErrorEnum.INTERNAL_SERVER_ERROR);
    }
    private static ResponseEntity<ArcErrorDTO> handleArcErrorException(RuntimeException ex, HttpServletRequest request, HttpStatus httpStatus, ArcErrorDTO.ErrorEnum errorEnum) {
        String message = ex.getMessage();
        log.info("A {} occurred handling request {}: HttpStatus {} - {}",
                ex.getClass(),
                getRequestDetails(request),
                httpStatus.value(),
                message);

        return ResponseEntity
                .status(httpStatus)
                .body(new ArcErrorDTO(errorEnum, message));
    }

    private static String getRequestDetails(HttpServletRequest request) {
        return "%s %s".formatted(request.getMethod(), request.getRequestURI());
    }
}
