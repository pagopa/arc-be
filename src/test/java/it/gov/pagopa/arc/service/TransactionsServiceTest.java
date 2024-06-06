package it.gov.pagopa.arc.service;

import it.gov.pagopa.arc.fakers.TransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final String FILTER = "DUMMY_FILTER";

    @Autowired
    private TransactionsService transactionsService;

    @Mock
    private BizEventsService bizEventsServiceMock;

    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsServiceImpl(bizEventsServiceMock);
    }

    @Test
    void givenPageSizeFilterWhenCallRetrieveTransactionsListThenReturnTransactionList() {
        //given
        TransactionDTO transactionDTO = TransactionDTOFaker.mockInstance(1, false);
        TransactionDTO transactionDTO2 = TransactionDTOFaker.mockInstance(2, true);

        List<TransactionDTO> transactions = List.of(
                transactionDTO,
                transactionDTO2
        );

        TransactionsListDTO expectedResult = TransactionsListDTO
                .builder()
                .transactions(transactions)
                .currentPage(1)
                .totalPages(1)
                .totalItems(10)
                .itemsForPage(SIZE)
                .build();

        Mockito.when(bizEventsServiceMock.retrieveTransactionsListFromBizEvents(PAGE,SIZE,FILTER)).thenReturn(expectedResult);
        //when
        TransactionsListDTO result = transactionsService.retrieveTransactionsList(PAGE, SIZE, FILTER);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTransactions().size());
        Assertions.assertEquals(transactions,expectedResult.getTransactions());
        Assertions.assertEquals(1, result.getCurrentPage());
        Assertions.assertEquals(2, result.getItemsForPage());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(10, result.getTotalItems());
        Mockito.verify(bizEventsServiceMock).retrieveTransactionsListFromBizEvents(anyInt(),anyInt(),anyString());
    }

    @Test
    void givenPageSizeFilterWhenCallRetrieveTransactionsListFromBizEventsThenReturnEmptyTransactionList() {
        //given
        List<TransactionDTO> transactions = new ArrayList<>();

        TransactionsListDTO expectedResult = TransactionsListDTO
                .builder()
                .transactions(transactions)
                .currentPage(1)
                .totalPages(1)
                .totalItems(10)
                .itemsForPage(SIZE)
                .build();

        Mockito.when(bizEventsServiceMock.retrieveTransactionsListFromBizEvents(PAGE,SIZE,FILTER)).thenReturn(expectedResult);
        //when
        TransactionsListDTO result = transactionsService.retrieveTransactionsList(PAGE, SIZE, FILTER);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTransactions().isEmpty());
        Assertions.assertEquals(1, result.getCurrentPage());
        Assertions.assertEquals(2, result.getItemsForPage());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(10, result.getTotalItems());
        Mockito.verify(bizEventsServiceMock).retrieveTransactionsListFromBizEvents(anyInt(),anyInt(),anyString());
    }
}