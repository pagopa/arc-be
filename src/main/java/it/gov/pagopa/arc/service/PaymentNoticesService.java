package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;

import java.time.LocalDate;

public interface PaymentNoticesService {
    PaymentNoticesListDTO retrievePaymentNotices(String userId, String userFiscalCode, LocalDate dueDate, Integer size, Integer page);
    PaymentNoticeDetailsDTO retrievePaymentNoticeDetails(String userId, String paTaxCode, String iupd);
}
