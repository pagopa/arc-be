package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionsServiceImpl implements TransactionsService {
    private final BizEventsService bizEventsService;

    public TransactionsServiceImpl(BizEventsService bizEventsService) {
        this.bizEventsService = bizEventsService;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsList(Integer page, Integer size, String filter) {
        log.info("[GET_TRANSACTIONS_LIST] The current user has requested to retrieve his list of transactions, with the current parameters: page {}, size {} and filter {}", page, size, filter);
        return bizEventsService.retrieveTransactionsListFromBizEvents(page,size,filter);
    }

    @Override
    public TransactionDetailsDTO retrieveTransactionDetails(String transactionId) {
        log.info("[GET_TRANSACTION_DETAILS] The current user has requested to retrieve transaction details for transaction with ID {}", transactionId);
        return bizEventsService.retrieveTransactionDetailsFromBizEvents(transactionId);
    }

    @Override
    public Resource retrieveTransactionReceipt(String transactionId) {
        log.info("[GET_TRANSACTION_RECEIPT] The current user has requested to retrieve transaction receipt for transaction with ID {}", transactionId);
        return bizEventsService.retrieveTransactionReceiptFromBizEvents(transactionId);
    }

}

