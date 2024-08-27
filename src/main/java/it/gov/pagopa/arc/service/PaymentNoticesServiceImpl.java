package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.service.pullpayment.PullPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class PaymentNoticesServiceImpl implements PaymentNoticesService{
    private final PullPaymentService pullPaymentService;

    public PaymentNoticesServiceImpl(PullPaymentService pullPaymentService) {
        this.pullPaymentService = pullPaymentService;
    }

    @Override
    public PaymentNoticesListDTO retrievePaymentNotices(LocalDate dueDate, Integer size, Integer page) {
        log.info("[GET_PAYMENT_NOTICES] The current user has requested to retrieve payment notices, with the current parameters: dueDate {}, size {} and page {}", dueDate, size, page);
        return pullPaymentService.retrievePaymentNoticesListFromPullPayment(dueDate,size,page);
    }
}
