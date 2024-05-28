package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsDTO;

public interface BizEventsConnector {
    BizEventsTransactionsDTO transactionsList(String fiscalCode, String continuationToken, int size);
}
