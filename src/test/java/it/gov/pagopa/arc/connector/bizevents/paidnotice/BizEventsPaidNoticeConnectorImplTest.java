package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
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
import java.util.Collections;
import java.util.Optional;

import static it.gov.pagopa.arc.config.WireMockConfig.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
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
    private String continuationToken;

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
    void givenHeaderAndParameterWhenCallBizEventsPaidNoticeConnectorThenReturnPaidNoticeList() throws IOException {
        //given

        //when
        Response response = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_OK", "CONTINUATION_TOKEN", 1, true, true, "TRANSACTION_DATE", "DESC");

        Optional<String> optionalContinuationToken = response.headers().get("x-continuation-token").stream().findFirst();

        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = objectMapper.readValue(response.body().asInputStream(), BizEventsPaidNoticeListDTO.class);


        //then

        assertEquals(1, bizEventsPaidNoticeListDTO.getNotices().size());
        assertEquals("1", bizEventsPaidNoticeListDTO.getNotices().get(0).getEventId());
        assertEquals("Comune di Milano", bizEventsPaidNoticeListDTO.getNotices().get(0).getPayeeName());
        assertEquals("MI_XXX", bizEventsPaidNoticeListDTO.getNotices().get(0).getPayeeTaxCode());
        assertEquals("180,00", bizEventsPaidNoticeListDTO.getNotices().get(0).getAmount());
        assertEquals("2024-03-27T13:07:25Z", bizEventsPaidNoticeListDTO.getNotices().get(0).getNoticeDate());
        assertFalse(bizEventsPaidNoticeListDTO.getNotices().get(0).getIsCart());
        assertTrue(bizEventsPaidNoticeListDTO.getNotices().get(0).getIsPayer());
        assertTrue(bizEventsPaidNoticeListDTO.getNotices().get(0).getIsDebtor());

        optionalContinuationToken.ifPresent(token -> {
            continuationToken = token;
            assertEquals("continuation-token", continuationToken);
        });


    }

    @Test
    void givenHeaderAndParameterWhenNotFoundThenReturnEmptyPaidNoticeList() throws IOException {
        //given
        //when
        Response response =  bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_NOT_FOUND", "CONTINUATION_TOKEN", 2, true, true, "TRANSACTION_DATE", "DESC");
        BizEventsPaidNoticeListDTO bizEventsPaidNoticeListDTO = objectMapper.readValue(response.body().asInputStream(), BizEventsPaidNoticeListDTO.class);

        //then
        Assertions.assertEquals(0, bizEventsPaidNoticeListDTO.getNotices().size());
        System.out.println(memoryAppender.getLoggedEvents().get(0).getFormattedMessage());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred while handling request getPaidNoticeList from biz-Events: HttpStatus 404"))
        );
    }

    @Test
    void givenHeaderAndParameterWhenSerializeThenThrow() throws IOException {
        //given
        objectMapper = Mockito.mock(ObjectMapper.class);
        BizEventsPaidNoticeRestClient bizEventsPaidNoticeRestClient = Mockito.mock(BizEventsPaidNoticeRestClient.class);
        Request originalRequest = Request.create(Request.HttpMethod.valueOf("GET"), "http://dummy-url", Collections.emptyMap(), null, null, null);
        doThrow(new JsonProcessingException("Serialization error") {}).when(objectMapper).writeValueAsBytes(Mockito.any());

        Mockito.when(bizEventsPaidNoticeRestClient.paidNoticeList("","fiscalCode", "continuationToken", 1, false, false, "orderBy", "ordering")).thenReturn(Response.builder().request(originalRequest).status(404).build());
        bizEventsPaidNoticeConnector = new BizEventsPaidNoticeConnectorImpl("",bizEventsPaidNoticeRestClient,objectMapper);
        //when
        //then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class, () ->  bizEventsPaidNoticeConnector.getPaidNoticeList("fiscalCode", "continuationToken", 1, false, false, "orderBy", "ordering"));

        assertEquals("Error during response serialization", exception.getMessage());

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