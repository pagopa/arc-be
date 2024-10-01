package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.core.io.Resource;

public interface BizEventsService {
    TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter);
    TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId);
    Resource retrieveTransactionReceiptFromBizEvents(String transactionId);
    NoticesListResponseDTO retrievePaidListFromBizEvents(String continuationToken, Integer size, Boolean isPayer, Boolean isDebtor, String orderBy, String ordering);
}
