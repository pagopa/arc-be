package it.gov.pagopa.arc.controller;

import it.gov.pagopa.arc.controller.generated.ArcPaymentNoticesApi;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.service.PaymentNoticesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class PaymentNoticesControllerImpl implements ArcPaymentNoticesApi {
    private final PaymentNoticesService paymentNoticesService;

    public PaymentNoticesControllerImpl(PaymentNoticesService paymentNoticesService) {
        this.paymentNoticesService = paymentNoticesService;
    }

    @Override
    public ResponseEntity<PaymentNoticesListDTO> getPaymentNotices(LocalDate dueDate, Integer size, Integer page) {
        PaymentNoticesListDTO paymentNoticesListDTO = paymentNoticesService.retrievePaymentNotices(dueDate, size, page);
        return new ResponseEntity<>(paymentNoticesListDTO, HttpStatus.OK);
    }
}
