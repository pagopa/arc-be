package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;

public interface BizEventsConnector {
    BizEventsTransactionsListDTO transactionsList(String fiscalCode, String continuationToken, int size);
}
