package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;

public interface BizEventsConnector {
    BizEventsTransactionsListDTO getTransactionsList(String fiscalCode, int size);
    BizEventsTransactionDetailsDTO getTransactionDetails(String fiscalCode, String transactionId);
}
