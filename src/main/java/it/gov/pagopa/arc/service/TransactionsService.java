package it.gov.pagopa.arc.service;


import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.core.io.Resource;

public interface TransactionsService {
    TransactionsListDTO retrieveTransactionsList(Integer page, Integer size, String filter);
    Resource retrieveTransactionReceipt(String transactionId);
}
