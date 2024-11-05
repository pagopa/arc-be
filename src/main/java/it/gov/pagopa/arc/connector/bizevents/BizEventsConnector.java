package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.springframework.core.io.Resource;

public interface BizEventsConnector {
    BizEventsTransactionsListDTO getTransactionsList(String fiscalCode, int size);
    Resource getTransactionReceipt(String fiscalCode, String transactionId);
}
