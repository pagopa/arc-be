package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.core.io.Resource;

public interface BizEventsService {
    TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter);
    TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId);
    Resource retrieveTransactionReceiptFromBizEvents(String transactionId);
    NoticesListResponseDTO retrievePaidListFromBizEvents(String userFiscalCode, NoticeRequestDTO noticeRequestDTO);
    NoticeDetailsDTO retrievePaidNoticeDetailsFromBizEvents(String userId, String userFiscalCode, String eventId);
}
