package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDetails2TransactionDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTO;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.utils.SecurityUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BizEventsServiceImpl implements BizEventsService{

    private final BizEventsConnector bizEventsConnector;
    private final BizEventsTransactionDTO2TransactionDTO transactionDTOMapper;
    private final BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper;
    private final BizEventsTransactionDetails2TransactionDetailsDTO transactionDetailsDTOMapper;
    private String userFiscalCode;

    public BizEventsServiceImpl(BizEventsConnector bizEventsConnector,
                                BizEventsTransactionDTO2TransactionDTO transactionDTOMapper,
                                BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper,
                                BizEventsTransactionDetails2TransactionDetailsDTO transactionDetailsDTOMapper) {
        this.bizEventsConnector = bizEventsConnector;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionsListDTOMapper = transactionsListDTOMapper;
        this.transactionDetailsDTOMapper = transactionDetailsDTOMapper;
    }

    private String getUserFiscalCode() {
        if (this.userFiscalCode == null) {
            this.userFiscalCode = SecurityUtils.getUserFiscalCode();
        }
        return this.userFiscalCode;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter) {
        String retrievedUserFiscalCode = getUserFiscalCode();
        List<TransactionDTO> transactions;
        BizEventsTransactionsListDTO bizEventsTransactionsList = bizEventsConnector.getTransactionsList(retrievedUserFiscalCode, size);
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

    @Override
    public TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId) {
        String retrievedUserFiscalCode = getUserFiscalCode();
        BizEventsTransactionDetailsDTO bizEventsTransactionDetails = bizEventsConnector.getTransactionDetails(retrievedUserFiscalCode, transactionId);
        return transactionDetailsDTOMapper.apply(bizEventsTransactionDetails);
    }

    @Override
    public Resource retrieveTransactionReceiptFromBizEvents(String transactionId) {
        String retrievedUserFiscalCode = getUserFiscalCode();
        return bizEventsConnector.getTransactionReceipt(retrievedUserFiscalCode, transactionId);
    }


}
