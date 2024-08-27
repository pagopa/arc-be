package it.gov.pagopa.arc.connector.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;

import java.time.LocalDate;
import java.util.List;

public interface PullPaymentConnector {
    List<PullPaymentNoticeDTO> getPaymentNotices(String fiscalCode, LocalDate dueDate, int limit, int page);
}
