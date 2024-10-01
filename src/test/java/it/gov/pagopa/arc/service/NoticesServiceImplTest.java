package it.gov.pagopa.arc.service;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.fakers.NoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoticesServiceImplTest {

    private static final int SIZE = 2;
    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String ORDER_BY = "TRANSACTION_DATE";
    private static final String ORDERING = "DESC";

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
    void givenRetrieveNoticesAndTokenWhenThen() {
        //given
        NoticeDTO noticeDTO = NoticeDTOFaker.mockInstance(1, false);
        List<NoticeDTO> listOfNoticeDTO = List.of(noticeDTO);

        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(listOfNoticeDTO).build();

        NoticesListResponseDTO noticesListResponseDTO = NoticesListResponseDTO.builder().noticesListDTO(noticesListDTO).continuationToken(CONTINUATION_TOKEN).build();

        Mockito.when(bizEventsServiceMock.retrievePaidListFromBizEvents(CONTINUATION_TOKEN, SIZE, true, true, ORDER_BY, ORDERING)).thenReturn(noticesListResponseDTO);
        //when
        NoticesListResponseDTO result = noticesService.retrieveNoticesAndToken(CONTINUATION_TOKEN, SIZE, true, true, ORDER_BY, ORDERING);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(noticesListResponseDTO.getNoticesListDTO().getNotices(), result.getNoticesListDTO().getNotices());
        Assertions.assertEquals(CONTINUATION_TOKEN, result.getContinuationToken());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage().contains("[GET_NOTICES_LIST] The current user has requested to retrieve his list of paid notices, with the current parameters: size 2, paidByMe true, registeredToMe true, orderBy TRANSACTION_DATE and ordering DESC"));
        Mockito.verify(bizEventsServiceMock).retrievePaidListFromBizEvents(anyString(),anyInt(),anyBoolean(),anyBoolean(),anyString(),anyString());
    }
}