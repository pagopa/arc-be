package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidResponseDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
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
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

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
        //when
        BizEventsPaidResponseDTO paidResponseDTO = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_OK", "CONTINUATION_TOKEN", 1, true, true, "TRANSACTION_DATE", "DESC");

        //then

        assertEquals(1, paidResponseDTO.getNotices().size());
        assertEquals("1", paidResponseDTO.getNotices().get(0).getEventId());
        assertEquals("Comune di Milano", paidResponseDTO.getNotices().get(0).getPayeeName());
        assertEquals("MI_XXX", paidResponseDTO.getNotices().get(0).getPayeeTaxCode());
        assertEquals("180,00", paidResponseDTO.getNotices().get(0).getAmount());
        assertEquals("2024-03-27T13:07:25Z", paidResponseDTO.getNotices().get(0).getNoticeDate());
        assertFalse(paidResponseDTO.getNotices().get(0).getIsCart());
        assertTrue(paidResponseDTO.getNotices().get(0).getIsPayer());
        assertTrue(paidResponseDTO.getNotices().get(0).getIsDebtor());
        assertEquals("continuation-token", paidResponseDTO.getContinuationToken());

    }

    @Test
    void givenHeaderAndParameterWhenNotFoundThenReturnEmptyPaidNoticeList() {
        //given
        //when
        BizEventsPaidResponseDTO paidResponseDTO = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_NOT_FOUND", "CONTINUATION_TOKEN", 2, true, true, "TRANSACTION_DATE", "DESC");

        //then
        Assertions.assertEquals(0, paidResponseDTO.getNotices().size());
        System.out.println(memoryAppender.getLoggedEvents().get(0).getFormattedMessage());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred while handling request getPaidNoticeList from biz-Events: HttpStatus 404"))
        );
        assertNull(paidResponseDTO.getContinuationToken());
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

        bizEventsPaidNoticeConnector = new BizEventsPaidNoticeConnectorImpl("", bizEventsPaidNoticeRestClient, objectMapper);

        // when & then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class, () ->
                bizEventsPaidNoticeConnector.getPaidNoticeList("fiscalCode", "continuationToken", 1, false, false, "orderBy", "ordering")
        );

        assertEquals("Error during processing: response body is null", exception.getMessage());
        Mockito.verify(bizEventsPaidNoticeRestClient).paidNoticeList("", "fiscalCode", "continuationToken", 1, false, false, "orderBy", "ordering");
    }

    @Test
    void givenHeaderAndParameterWhenThrowIOExceptionThenThrow() throws IOException {
        // given
        BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient = Mockito.mock(BizEventsPaidNoticeRestClient.class);
        Request originalRequest = Request.create(Request.HttpMethod.GET, "http://dummy-url", Collections.emptyMap(), null, null, null);

        ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
        bizEventsPaidNoticeConnector = new BizEventsPaidNoticeConnectorImpl("", bizEventsPaidNoticeRestClient, mockObjectMapper);

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

        // when & then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class, () ->
                bizEventsPaidNoticeConnector.getPaidNoticeList("fiscalCode", "continuationToken", 1, false, false, "orderBy", "ordering")
        );

        assertEquals("Error reading or deserializing the response body", exception.getMessage());
    }



    @Test
    void givenHeaderAndParameterWhenErrorThenThrowBizEventsInvocationException() {
        //When
        //Then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class,
                () -> bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_ERROR", "CONTINUATION_TOKEN", 2, true, true, "TRANSACTION_DATE", "DESC"));
        Assertions.assertEquals("An error occurred handling request from biz-Events",exception.getMessage());
    }
}