package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.dto.NoticeRequestDTO;
import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.dto.mapper.bizevents.paidnotice.BizEventsPaidNoticeDTO2NoticesListResponseDTOMapper;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import it.gov.pagopa.arc.fakers.NoticeRequestDTOFaker;
import it.gov.pagopa.arc.model.generated.NoticeDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import it.gov.pagopa.arc.utils.MemoryAppender;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.arc.config.WireMockConfig.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = WireMockConfig.WireMockInitializer.class,
        classes = {
                BizEventsPaidNoticeConnectorImpl.class,
                ObjectMapper.class,
                FeignConfig.class,
                BizEventsPaidNoticeRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(properties = {
        "rest-client.biz-events.paid-notice.api-key=x_api_key0",
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "rest-client.biz-events.paid-notice.baseUrl=bizEventsPaidNoticeMock"
})
class BizEventsPaidNoticeConnectorImplTest {
    @Autowired
    private BizEventsPaidNoticeConnector bizEventsPaidNoticeConnector;
    @MockBean
    private BizEventsPaidNoticeDTO2NoticesListResponseDTOMapper bizEventsPaidNoticeDTO2NoticesListResponseDTOMapperMock;
    private MemoryAppender memoryAppender;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnectorImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
        objectMapper =  TestUtils.objectMapper;
    }

    @Test
    void givenHeaderAndParameterWhenCallBizEventsPaidNoticeConnectorThenReturnPaidNoticeList() {
        //given
        BizEventsPaidNoticeDTO bizEventsPaidNoticeDTO = BizEventsPaidNoticeDTO.builder()
                .eventId("1")
                .payeeName("Comune di Milano")
                .payeeTaxCode("MI_XXX")
                .amount("180,00")
                .noticeDate("2024-03-27T13:07:25Z")
                .isCart(false)
                .isDebtor(true)
                .isPayer(true)
                .build();

        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = BizEventsPaidNoticeListDTO.builder().notices(List.of(bizEventsPaidNoticeDTO)).build();

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .eventId("1")
                .payeeName("Comune di Milano")
                .payeeTaxCode("MI_XXX")
                .amount(18000L)
                .noticeDate(ZonedDateTime.parse("2024-03-27T13:07:25Z"))
                .isCart(false)
                .paidByMe(true)
                .registeredToMe(true)
                .build();

        NoticesListDTO noticesListDTO = NoticesListDTO.builder().notices(List.of(noticeDTO)).build();

        Mockito.when(bizEventsPaidNoticeDTO2NoticesListResponseDTOMapperMock.toNoticeListDTO(bizEventsPaidNoticeListDTO)).thenReturn(noticesListDTO);
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTO.builder()
                .continuationToken("CONTINUATION_TOKEN")
                .size(1)
                .paidByMe(true)
                .registeredToMe(true)
                .orderBy("TRANSACTION_DATE")
                .orderBy(null)
                .ordering("DESC")
                .build();

        //when
        NoticesListResponseDTO result = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_OK", noticeRequestDTO);

        //then

        List<NoticeDTO> notices = result.getNoticesListDTO().getNotices();
        assertEquals(1, notices.size());
        assertEquals("1", notices.get(0).getEventId());
        assertEquals("Comune di Milano", notices.get(0).getPayeeName());
        assertEquals("MI_XXX", notices.get(0).getPayeeTaxCode());
        assertEquals(18000L, notices.get(0).getAmount());
        assertEquals(ZonedDateTime.parse("2024-03-27T13:07:25Z") , notices.get(0).getNoticeDate());
        assertFalse(notices.get(0).getIsCart());
        assertTrue(notices.get(0).getPaidByMe());
        assertTrue(notices.get(0).getRegisteredToMe());
        assertEquals("continuation-token", result.getContinuationToken());

    }

    @Test
    void givenHeaderAndParameterWhenNotFoundThenReturnEmptyPaidNoticeList() {
        //given
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        //when
        NoticesListResponseDTO noticesListResponseDTO = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_NOT_FOUND", noticeRequestDTO);

        //then
        Assertions.assertEquals(0, noticesListResponseDTO.getNoticesListDTO().getNotices().size());
        System.out.println(memoryAppender.getLoggedEvents().get(0).getFormattedMessage());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred while handling request getPaidNoticeList from biz-Events: HttpStatus 404"))
        );
        assertNull(noticesListResponseDTO.getContinuationToken());
    }

    @Test
    void givenHeaderAndParameterWhenResponseBodyNullThenThrow() {
        // given
        BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient = Mockito.mock(BizEventsPaidNoticeRestClient.class);
        Request originalRequest = Request.create(Request.HttpMethod.GET, "http://dummy-url", Collections.emptyMap(), null, null, null);

        Response mockResponse = Response.builder().status(200).request(originalRequest).build();
        Mockito.when(bizEventsPaidNoticeRestClient.paidNoticeList(   anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyBoolean(),
                anyBoolean(),
                anyString(),
                anyString()))
                .thenReturn(mockResponse);

        bizEventsPaidNoticeConnector = new BizEventsPaidNoticeConnectorImpl("", bizEventsPaidNoticeRestClient, bizEventsPaidNoticeDTO2NoticesListResponseDTOMapperMock, objectMapper);
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        // when & then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class, () ->
                bizEventsPaidNoticeConnector.getPaidNoticeList("fiscalCode", noticeRequestDTO)
        );

        assertEquals("Error during processing: response body is null", exception.getMessage());
        Mockito.verify(bizEventsPaidNoticeRestClient).paidNoticeList("", "fiscalCode", "continuation-token", 2, true, true, "TRANSACTION_DATE", "DESC");
    }

    @Test
    void givenHeaderAndParameterWhenThrowIOExceptionThenThrow() throws IOException {
        // given
        BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient = Mockito.mock(BizEventsPaidNoticeRestClient.class);
        Request originalRequest = Request.create(Request.HttpMethod.GET, "http://dummy-url", Collections.emptyMap(), null, null, null);

        ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
        bizEventsPaidNoticeConnector = new BizEventsPaidNoticeConnectorImpl("", bizEventsPaidNoticeRestClient, bizEventsPaidNoticeDTO2NoticesListResponseDTOMapperMock, mockObjectMapper);

        doThrow(new IOException("Deserialization error"))
                .when(mockObjectMapper)
                .readValue(any(InputStream.class), eq(BizEventsPaidNoticeListDTO.class));

        Response.Body mockBody = Mockito.mock(Response.Body.class);
        InputStream mockInputStream = Mockito.mock(InputStream.class);

        Mockito.when(mockBody.asInputStream()).thenReturn(mockInputStream);

        Response mockResponse = Response.builder()
                .status(200)
                .request(originalRequest)
                .body(mockBody)
                .build();

        Mockito.when(bizEventsPaidNoticeRestClient.paidNoticeList(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyInt(),
                        anyBoolean(),
                        anyBoolean(),
                        anyString(),
                        anyString()))
                .thenReturn(mockResponse);
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        // when & then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class, () ->
                bizEventsPaidNoticeConnector.getPaidNoticeList("fiscalCode", noticeRequestDTO )
        );

        assertEquals("Error reading or deserializing the response body", exception.getMessage());
    }



    @Test
    void givenHeaderAndParameterWhenErrorThenThrowBizEventsInvocationException() {
        NoticeRequestDTO noticeRequestDTO = NoticeRequestDTOFaker.mockInstance();
        //When
        //Then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class,
                () -> bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_ERROR", noticeRequestDTO));
        Assertions.assertEquals("An error occurred handling request from biz-Events",exception.getMessage());
    }

}