package it.gov.pagopa.arc.exception;

import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import it.gov.pagopa.arc.exception.custom.BizEventsReceiptNotFoundException;
import it.gov.pagopa.arc.exception.custom.BizEventsTransactionNotFoundException;
import it.gov.pagopa.arc.model.generated.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ArcExceptionHandler {

    @ExceptionHandler(BizEventsInvocationException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsInvocationException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.ErrorEnum.GENERIC_ERROR);
    }

    @ExceptionHandler(BizEventsTransactionNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsTransactionNotFoundException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.NOT_FOUND, ErrorDTO.ErrorEnum.TRANSACTION_NOT_FOUND_ERROR);
    }

    @ExceptionHandler(BizEventsReceiptNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsReceiptNotFoundException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.NOT_FOUND, ErrorDTO.ErrorEnum.RECEIPT_NOT_FOUND_ERROR);
    }

    private static ResponseEntity<ErrorDTO> handleArcErrorException(RuntimeException ex, HttpServletRequest request, HttpStatus httpStatus, ErrorDTO.ErrorEnum errorEnum) {
        String message = ex.getMessage();
        log.info("A {} occurred handling request {}: HttpStatus {} - {}",
                ex.getClass(),
                getRequestDetails(request),
                httpStatus.value(),
                message);

        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorDTO(errorEnum, message));
    }

    private static String getRequestDetails(HttpServletRequest request) {
        return "%s %s".formatted(request.getMethod(), request.getRequestURI());
    }
}
