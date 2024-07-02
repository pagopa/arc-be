package it.gov.pagopa.arc.service;


import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;

public interface TransactionsService {
    TransactionsListDTO retrieveTransactionsList(Integer page, Integer size, String filter);
    TransactionDetailsDTO retrieveTransactionDetails(String transactionId);
}
