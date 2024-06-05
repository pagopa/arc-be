package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTO;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionsServiceImpl implements TransactionsService {
    private final String fakeFiscalCode;
    private final BizEventsConnector bizEventsConnector;
    private final BizEventsTransactionDTO2TransactionDTO transactionDTOMapper;
    private final BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper;


    public TransactionsServiceImpl(
            @Value("${rest-client.biz-events.fake-fiscal-code}") String fakeFiscalCode,
            BizEventsConnector bizEventsConnector,
            BizEventsTransactionDTO2TransactionDTO transactionDTOMapper,
            BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper) {
        this.fakeFiscalCode = fakeFiscalCode;
        this.bizEventsConnector = bizEventsConnector;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionsListDTOMapper = transactionsListDTOMapper;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsList(Integer page, Integer size, String filter) {
        List<TransactionDTO> transactions;
        BizEventsTransactionsListDTO bizEventsTransactionsList = bizEventsConnector.getTransactionsList(fakeFiscalCode, size);
        if(!bizEventsTransactionsList.getTransactions().isEmpty()) {
             transactions = bizEventsTransactionsList
                    .getTransactions()
                    .stream()
                    .map(transactionDTOMapper::apply)
                    .toList();
        } else {
            transactions = new ArrayList<>();
        }
        return transactionsListDTOMapper.apply(transactions, size);
    }
}

