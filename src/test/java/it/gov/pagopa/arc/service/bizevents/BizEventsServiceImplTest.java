package it.gov.pagopa.arc.service.bizevents;

import it.gov.pagopa.arc.connector.bizevents.BizEventsConnector;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnector;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDTO2TransactionDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionDetails2TransactionDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.BizEventsTransactionsListDTO2TransactionsListDTOMapper;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.fakers.NoticeRequestDTOFaker;
import it.gov.pagopa.arc.fakers.TransactionDTOFaker;
import it.gov.pagopa.arc.fakers.TransactionDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsTransactionDTOFaker;
import it.gov.pagopa.arc.fakers.bizEvents.BizEventsTransactionDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BizEventsServiceImplTest {
    private static final int PAGE = 1;
    private static final int SIZE = 2;
    private static final String FILTER = "DUMMY_FILTER";
    private static final String TRANSACTION_ID = "TRANSACTION_ID";
    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final String CONTINUATION_TOKEN = "continuation-token";

    private BizEventsService bizEventsService;

    @Mock
    private BizEventsConnector bizEventsConnectorMock;
    @Mock
    private BizEventsTransactionDTO2TransactionDTOMapper transactionDTOMapperMock;
    @Mock
    private BizEventsTransactionsListDTO2TransactionsListDTOMapper transactionsListDTOMapperMock;
    @Mock
    private BizEventsTransactionDetails2TransactionDetailsDTOMapper transactionDetailsDTOMapperMock;
    @Mock
    private BizEventsPaidNoticeConnector bizEventsPaidNoticeConnectorMock;

    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                IamUserInfoDTOFaker.mockInstance(), null, null);
        authentication.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        bizEventsService = new BizEventsServiceImpl(
                bizEventsConnectorMock ,
                transactionDTOMapperMock,
                transactionsListDTOMapperMock,
                transactionDetailsDTOMapperMock,
                bizEventsPaidNoticeConnectorMock);
    }

    @AfterEach
    void afterMethod() {
        Mockito.verifyNoMoreInteractions(
                bizEventsConnectorMock,
                transactionDTOMapperMock,
                transactionsListDTOMapperMock,
                transactionDetailsDTOMapperMock,
                bizEventsPaidNoticeConnectorMock
        );
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenPageSizeFilterWhenCallRetrieveTransactionsListFromBizEventsThenReturnTransactionList() {
        //given
        BizEventsTransactionDTO bizEventsTransactionDTO = BizEventsTransactionDTOFaker.mockInstance(1, false);
        BizEventsTransactionDTO bizEventsTransactionDTO2 = BizEventsTransactionDTOFaker.mockInstance(2, true);

        TransactionDTO transactionDTO = TransactionDTOFaker.mockInstance(1, false);
        TransactionDTO transactionDTO2 = TransactionDTOFaker.mockInstance(2, true);

        List<BizEventsTransactionDTO> bizEventsTransactionDTOList = List.of(
                bizEventsTransactionDTO,
                bizEventsTransactionDTO2
        );

        List<TransactionDTO> transactions = List.of(
                transactionDTO,
                transactionDTO2
        );

        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = BizEventsTransactionsListDTO
                .builder()
                .transactions(bizEventsTransactionDTOList)
                .build();

        TransactionsListDTO expectedResult = TransactionsListDTO
                .builder()
                .transactions(transactions)
                .currentPage(1)
                .totalPages(1)
                .totalItems(10)
                .itemsForPage(SIZE)
                .build();

        when(bizEventsConnectorMock.getTransactionsList(DUMMY_FISCAL_CODE,SIZE)).thenReturn(bizEventsTransactionsListDTO);
        when(transactionDTOMapperMock.apply(bizEventsTransactionDTO)).thenReturn(transactionDTO);
        when(transactionDTOMapperMock.apply(bizEventsTransactionDTO2)).thenReturn(transactionDTO2);
        when(transactionsListDTOMapperMock.apply(transactions,SIZE)).thenReturn(expectedResult);

        //when
        TransactionsListDTO result = bizEventsService.retrieveTransactionsListFromBizEvents(PAGE, SIZE, FILTER);

        //then
        Assertions.assertNotNull(result);
        assertEquals(2, result.getTransactions().size());
        assertEquals(transactions,expectedResult.getTransactions());
        assertEquals(1, result.getCurrentPage());
        assertEquals(2, result.getItemsForPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getTotalItems());

        Mockito.verify(transactionDTOMapperMock,Mockito.times(2)).apply(any());
    }

    @Test
    void givenPageSizeFilterWhenCallRetrieveTransactionsListThenReturnEmptyTransactionList() {
        //given
        List<TransactionDTO> transactions = new ArrayList<>();

        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = BizEventsTransactionsListDTO
                .builder()
                .transactions(new ArrayList<>())
                .build();

        TransactionsListDTO expectedResult = TransactionsListDTO
                .builder()
                .transactions(transactions)
                .currentPage(1)
                .totalPages(1)
                .totalItems(10)
                .itemsForPage(SIZE)
                .build();

        when(bizEventsConnectorMock.getTransactionsList(DUMMY_FISCAL_CODE,SIZE)).thenReturn(bizEventsTransactionsListDTO);
        when(transactionsListDTOMapperMock.apply(transactions,SIZE)).thenReturn(expectedResult);
        //when
        TransactionsListDTO result = bizEventsService.retrieveTransactionsListFromBizEvents(PAGE, SIZE, FILTER);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTransactions().isEmpty());
        assertEquals(1, result.getCurrentPage());
        assertEquals(2, result.getItemsForPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(10, result.getTotalItems());
    }

    @Test
    void givenTransactionIdWhenCallRetrieveTransactionDetailsFromBizEventsThenReturnTransactionDetails() {
        //given
        BizEventsTransactionDetailsDTO bizEventsTransactionDetails = BizEventsTransactionDetailsDTOFaker.mockInstance();
        TransactionDetailsDTO expectedResult = TransactionDetailsDTOFaker.mockInstance();

        when(bizEventsConnectorMock.getTransactionDetails(DUMMY_FISCAL_CODE,TRANSACTION_ID)).thenReturn(bizEventsTransactionDetails);
        when(transactionDetailsDTOMapperMock.apply(bizEventsTransactionDetails)).thenReturn(expectedResult);
        //when
        TransactionDetailsDTO result = bizEventsService.retrieveTransactionDetailsFromBizEvents(TRANSACTION_ID);

        //then
        Assertions.assertNotNull(result);
        assertEquals(2, result.getCarts().size());
        assertEquals(expectedResult.getInfoTransaction(), result.getInfoTransaction());
        assertEquals(expectedResult.getCarts(), result.getCarts());
        assertEquals(expectedResult, result);

    }

    @Test
    void givenTransactionIdWhenCallRetrieveTransactionReceiptFromBizEventsThenReturnTransactionReceipt() throws IOException {
        //given
        Resource receipt = new FileSystemResource("src/test/resources/stub/__files/testReceiptPdfFile.pdf");

        when(bizEventsConnectorMock.getTransactionReceipt(DUMMY_FISCAL_CODE,TRANSACTION_ID)).thenReturn(receipt);
        //when
        Resource result = bizEventsService.retrieveTransactionReceiptFromBizEvents(TRANSACTION_ID);

        //then
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] resultAsByteArray = result.getContentAsByteArray();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(resultAsByteArray, expectedContent);
    }

    @Test
    void givenParametersWhenCallRetrievePaidListFromBizEventsThenReturnPaidList() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        List<NoticeDTO> listOfNoticeDTO = List.of(noticeDTO);

        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(listOfNoticeDTO).build();

        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder().noticesListDTO(noticesListDTO).continuationToken(CONTINUATION_TOKEN).build();
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        when(bizEventsPaidNoticeConnectorMock.getPaidNoticeList(DUMMY_FISCAL_CODE, noticeRequestDTO)).thenReturn(noticesListResponseDTO);
        //when
        NoticesListResponseDTO result = bizEventsService.retrievePaidListFromBizEvents(DUMMY_FISCAL_CODE, noticeRequestDTO);

        //then
        Assertions.assertNotNull(result);
        assertEquals(1, result.getNoticesListDTO().getNotices().size());
        assertEquals(noticesListResponseDTO.getNoticesListDTO().getNotices().get(0), result.getNoticesListDTO().getNotices().get(0));
        assertEquals(CONTINUATION_TOKEN, result.getContinuationToken());

    }


}