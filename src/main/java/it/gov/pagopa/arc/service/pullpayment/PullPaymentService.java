package it.gov.pagopa.arc.service.pullpayment;

import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;

import java.time.LocalDate;

public interface PullPaymentService {
    PaymentNoticesListDTO retrievePaymentNoticesListFromPullPayment(LocalDate dueDate, Integer size, Integer page);
}
