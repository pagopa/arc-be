package it.gov.pagopa.arc.controller;


import it.gov.pagopa.arc.dto.TransactionDTO;
import it.gov.pagopa.arc.service.TransactionsService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionsControllerImpl implements TransactionsController {
    private final TransactionsService transactionsService;

    public TransactionsControllerImpl(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @Override
    public List<TransactionDTO> getTransactionsList(String fiscalCode, String continuationToken, int size) {
        return transactionsService.retrieveTransactionsList(fiscalCode);
    }
}
