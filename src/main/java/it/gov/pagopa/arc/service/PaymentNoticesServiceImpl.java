package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import it.gov.pagopa.arc.service.gpd.GPDService;
import it.gov.pagopa.arc.service.pullpayment.PullPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class PaymentNoticesServiceImpl implements PaymentNoticesService{
    private final PullPaymentService pullPaymentService;
    private final GPDService gpdService;

    public PaymentNoticesServiceImpl(PullPaymentService pullPaymentService , GPDService gpdService) {
        this.pullPaymentService = pullPaymentService;
        this.gpdService = gpdService;
    }

    @Override
    public PaymentNoticesListDTO retrievePaymentNotices(String userId, String userFiscalCode, LocalDate dueDate, Integer size, Integer page) {
        log.info("[GET_PAYMENT_NOTICES] The current user with user id : {}, has requested to retrieve payment notices, with the current parameters: dueDate {}, size {} and page {}", userId, dueDate, size, page);
        return pullPaymentService.retrievePaymentNoticesListFromPullPayment(userFiscalCode, dueDate,size,page);
    }

    @Override
    public PaymentNoticeDetailsDTO retrievePaymentNoticeDetails(String userId, String paTaxCode, String iupd) {
        log.info("[GET_PAYMENT_NOTICE_DETAILS] The current user with user id : {}, has requested to retrieve payment notice details, with paTaxCode {} and iupd {}", userId, paTaxCode, iupd);
        return gpdService.retrievePaymentNoticeDetailsFromGPD(userId, paTaxCode, iupd);
    }
}
