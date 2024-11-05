package it.gov.pagopa.arc.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.fakers.TransactionDTOFaker;
import it.gov.pagopa.arc.model.generated.TransactionDTO;
import it.gov.pagopa.arc.model.generated.TransactionsListDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import it.gov.pagopa.arc.utils.MemoryAppender;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final String FILTER = "DUMMY_FILTER";
    private static final String TRANSACTION_ID = "TRANSACTION_ID";

    private MemoryAppender memoryAppender;

    @Autowired
    private TransactionsService transactionsService;

    @Mock
    private BizEventsService bizEventsServiceMock;

    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsServiceImpl(bizEventsServiceMock);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.service.TransactionsServiceImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
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
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[GET_TRANSACTIONS_LIST] The current user has requested to retrieve his list of transactions, with the current parameters: page 1, size 2 and filter DUMMY_FILTER"));
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

    @Test
    void givenTransactionIdWhenCallRetrieveTransactionReceiptThenReturnTransactionReceipt() throws IOException {
        //given
        Resource receipt = new FileSystemResource("src/test/resources/stub/__files/testReceiptPdfFile.pdf");
        Mockito.when(bizEventsServiceMock.retrieveTransactionReceiptFromBizEvents(TRANSACTION_ID)).thenReturn(receipt);
        //when
        Resource result = transactionsService.retrieveTransactionReceipt(TRANSACTION_ID);
        //then
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] resultAsByteArray = result.getContentAsByteArray();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(expectedContent, resultAsByteArray);
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[GET_TRANSACTION_RECEIPT] The current user has requested to retrieve transaction receipt for transaction with ID TRANSACTION_ID"));
        Mockito.verify(bizEventsServiceMock).retrieveTransactionReceiptFromBizEvents(anyString());
    }
}