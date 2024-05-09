package it.gov.pagopa.arc.service;


import it.gov.pagopa.arc.dto.TransactionDTO;

import java.util.List;

public interface TransactionsService {
    List<TransactionDTO> retrieveTransactionsList(String fiscalCode);
}
