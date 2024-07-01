package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsCartItem2CartItemDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDetails2TransactionDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTO;
import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionDetailsDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BizEventsServiceImpl implements BizEventsService{
    private final String fakeFiscalCode;
    private final BizEventsConnector bizEventsConnector;
    private final BizEventsTransactionDTO2TransactionDTO transactionDTOMapper;
    private final BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper;
    private final BizEventsCartItem2CartItemDTO cartItemDTOMapper;
    private final BizEventsTransactionDetails2TransactionDetailsDTO transactionDetailsDTOMapper;

    public BizEventsServiceImpl(@Value("${rest-client.biz-events.fake-fiscal-code}") String fakeFiscalCode,
                                BizEventsConnector bizEventsConnector,
                                BizEventsTransactionDTO2TransactionDTO transactionDTOMapper,
                                BizEventsTransactionsListDTO2TransactionsListDTO transactionsListDTOMapper,
                                BizEventsCartItem2CartItemDTO cartItemDTOMapper,
                                BizEventsTransactionDetails2TransactionDetailsDTO transactionDetailsDTOMapper) {
        this.fakeFiscalCode = fakeFiscalCode;
        this.bizEventsConnector = bizEventsConnector;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionsListDTOMapper = transactionsListDTOMapper;
        this.cartItemDTOMapper = cartItemDTOMapper;
        this.transactionDetailsDTOMapper = transactionDetailsDTOMapper;
    }

    @Override
    public TransactionsListDTO retrieveTransactionsListFromBizEvents(Integer page, Integer size, String filter) {
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

    @Override
    public TransactionDetailsDTO retrieveTransactionDetailsFromBizEvents(String transactionId) {
        BizEventsTransactionDetailsDTO bizEventsTransactionDetails = bizEventsConnector.getTransactionDetails(fakeFiscalCode, transactionId);
        List<CartItemDTO> carts = bizEventsTransactionDetails
                    .getBizEventsCartsDTO()
                    .stream()
                    .map(cartItemDTOMapper::mapCart)
                    .toList();

        return transactionDetailsDTOMapper.apply(bizEventsTransactionDetails, carts);
    }
}
