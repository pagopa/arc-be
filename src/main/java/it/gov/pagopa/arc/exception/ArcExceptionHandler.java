package it.gov.pagopa.arc.exception;

import it.gov.pagopa.arc.exception.custom.*;
import it.gov.pagopa.arc.model.generated.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @ExceptionHandler(BizEventsInvalidAmountException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsInvalidAmountException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.ErrorEnum.INVALID_AMOUNT);
    }

    @ExceptionHandler(BizEventsInvalidDateException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsInvalidDateException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.ErrorEnum.INVALID_DATE);
    }

    @ExceptionHandler(PullPaymentInvalidRequestException.class)
    public ResponseEntity<ErrorDTO> handlePullPaymentInvalidRequestException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.ErrorEnum.INVALID_REQUEST);
    }

    @ExceptionHandler(PullPaymentInvocationException.class)
    public ResponseEntity<ErrorDTO> handlePullPaymentInvocationException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.ErrorEnum.GENERIC_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDTO> handlePullInvalidTokenException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.UNAUTHORIZED, ErrorDTO.ErrorEnum.AUTH_USER_UNAUTHORIZED);
    }

    @ExceptionHandler(ZendeskAssistanceInvalidUserEmailException.class)
    public ResponseEntity<ErrorDTO> handleZendeskAssistanceInvalidUserEmailException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.ErrorEnum.INVALID_EMAIL);
    }

    @ExceptionHandler(BizEventsPaidNoticeNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleBizEventsNoticeNotFoundException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.NOT_FOUND, ErrorDTO.ErrorEnum.NOTICE_NOT_FOUND_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleGenericRuntimeException(RuntimeException ex, HttpServletRequest request){
        return handleArcErrorException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.ErrorEnum.GENERIC_ERROR);
    }

    private static ResponseEntity<ErrorDTO> handleArcErrorException(RuntimeException ex, HttpServletRequest request, HttpStatus httpStatus, ErrorDTO.ErrorEnum errorEnum) {
        String message = ex.getMessage();
        log.info("A {} occurred handling request {}: HttpStatus {} - {}",
                ex.getClass(),
                getRequestDetails(request),
                httpStatus.value(),
                message);

        log.error("Exception occurred: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDTO(errorEnum, message));
    }

    private static String getRequestDetails(HttpServletRequest request) {
        return "%s %s".formatted(request.getMethod(), request.getRequestURI());
    }
}
