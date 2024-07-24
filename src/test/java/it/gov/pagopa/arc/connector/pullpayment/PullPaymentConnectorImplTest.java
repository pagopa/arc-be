package it.gov.pagopa.arc.connector.pullpayment;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.WireMock.BaseWireMock;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.fakers.pullPayment.PullPaymentNoticeDTOFaker;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static it.gov.pagopa.arc.WireMock.BaseWireMock.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;

@ContextConfiguration(
        classes = {
                PullPaymentConnectorImpl.class,
                FeignConfig.class,
                PullPaymentRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(
        properties = {
                "rest-client.pull-payment.api-key=x_api_key0",
                WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "rest-client.pull-payment.baseUrl=pullPaymentMock"
        })
class PullPaymentConnectorImplTest extends BaseWireMock {
    @Autowired
    private PullPaymentConnector pullPaymentConnector;

    private MemoryAppender memoryAppender;

    @BeforeEach
    void setUp() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.connector.pullpayment.PullPaymentConnectorImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenHeaderAndParameterWhenCallPullPaymentConnectorThenReturnPaymentNoticesList() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO = PullPaymentNoticeDTOFaker.mockInstance();
        //when
        List<PullPaymentNoticeDTO> result = pullPaymentConnector.getPaymentNotices("DUMMY_FISCAL_CODE", LocalDate.now(), 10, 0);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pullPaymentNoticeDTO,result.get(0));

    }

    @Test
    void givenHeaderAndParameterWhenCallPullPaymentConnectorThenReturnEmptyPaymentNotices() {
        //given
        //when
        List<PullPaymentNoticeDTO> result = pullPaymentConnector.getPaymentNotices("DUMMY_FISCAL_CODE_NOT_FOUND", LocalDate.now(), 10, 0);

        //then
        Assertions.assertEquals(0,result.size());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred handling request getPaymentNotices from pull-payment: HttpStatus 404 - [404 Not Found]"))
        );
    }

    @Test
    void givenHeaderAndParameterWhenErrorThenThrowPullPaymentInvocationException() {
        //When
        //Then
        Assertions.assertThrows(RuntimeException.class,
                ()-> pullPaymentConnector.getPaymentNotices("DUMMY_FISCAL_CODE_ERROR", LocalDate.now(), 10, 0));
    }

}