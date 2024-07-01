package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;

public interface BizEventsService {
    TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter);
    TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId);
}
