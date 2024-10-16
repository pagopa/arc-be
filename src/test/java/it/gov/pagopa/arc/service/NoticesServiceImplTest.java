package it.gov.pagopa.arc.service;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.fakers.NoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.NoticeRequestDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.service.bizevents.BizEventsService;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class NoticesServiceImplTest {

    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String DUMMY_FISCAL_CODE = "FISCAL-CODE789456";
    private static final String USER_ID = "user_id";

    @Mock
    private BizEventsService bizEventsServiceMock;

    private MemoryAppender memoryAppender;

    NoticesService noticesService;

    @BeforeEach
    void setUp() {
        noticesService = new NoticesServiceImpl(bizEventsServiceMock);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.service.NoticesServiceImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenRequestWhenRetrieveNoticesAndTokenThenReturnNoticesListResponseDTO () {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        List<NoticeDTO> listOfNoticeDTO = List.of(noticeDTO);

        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(listOfNoticeDTO).build();

        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder().noticesListDTO(noticesListDTO).continuationToken(CONTINUATION_TOKEN).build();
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();

        Mockito.when(bizEventsServiceMock.retrievePaidListFromBizEvents(DUMMY_FISCAL_CODE, noticeRequestDTO)).thenReturn(noticesListResponseDTO);
        //when
        NoticesListResponseDTO result = noticesService.retrieveNoticesAndToken(DUMMY_FISCAL_CODE, USER_ID, noticeRequestDTO);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(noticesListResponseDTO.getNoticesListDTO().getNotices(), result.getNoticesListDTO().getNotices());
        Assertions.assertEquals(CONTINUATION_TOKEN, result.getContinuationToken());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[GET_NOTICES_LIST] The current user with id : user_id, has requested to retrieve his list of paid notices, with the current parameters: size 2, paidByMe true, registeredToMe true, orderBy TRANSACTION_DATE and ordering DESC"));
    }

    @Test
    void givenRequestWhenRetrieveNoticeDetailsThenReturnNoticeDetailsDTO(){
        //given
        NoticeDetailsDTO noticeDetailsDTO = NoticeDetailsDTOFaker.mockInstance();

        Mockito.when(bizEventsServiceMock.retrievePaidNoticeDetailsFromBizEvents(USER_ID, DUMMY_FISCAL_CODE, "EVENT_ID")).thenReturn(noticeDetailsDTO);
        //When
        NoticeDetailsDTO result = noticesService.retrieveNoticeDetails(USER_ID, DUMMY_FISCAL_CODE, "EVENT_ID");
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(noticeDetailsDTO, result);
    }

    @Test
    void givenEventIdWhenCallRetrieveNoticeReceiptThenReturnNoticeReceipt() throws IOException {
        //given
        Resource receipt = new FileSystemResource("src/test/resources/stub/__files/testReceiptPdfFile.pdf");
        Mockito.when(bizEventsServiceMock.retrievePaidNoticeReceiptFromBizEvents(USER_ID, DUMMY_FISCAL_CODE, "EVENT_ID")).thenReturn(receipt);
        //when
        Resource result = noticesService.retrieveNoticeReceipt(USER_ID, DUMMY_FISCAL_CODE, "EVENT_ID");
        //then
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] resultAsByteArray = result.getContentAsByteArray();

        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(expectedContent, resultAsByteArray);
    }
}