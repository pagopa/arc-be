package it.gov.pagopa.arc.connector.bizevents;

import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;

public interface BizEventsConnector {
    BizEventsTransactionsListDTO getTransactionsList(String fiscalCode, String continuationToken, int size);
}
