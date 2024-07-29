package it.gov.pagopa.arc.connector.pullpayment;

import ch.qos.logback.classic.LoggerContext;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentNoticeDTOFaker;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = PullPaymentConnectorImplTest.WireMockInitializer.class,
        classes = {
                PullPaymentConnectorImpl.class,
                FeignConfig.class,
                PullPaymentRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(
        properties = {
                "rest-client.pull-payment.api-key=x_api_key0"
        })
class PullPaymentConnectorImplTest {
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
        PullPaymentNoticeDTO pullPaymentNoticeDTO = PullPaymentNoticeDTOFaker.mockInstance(true);
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

    public static class WireMockInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().usingFilesUnderClasspath("src/test/resources/stub").dynamicPort());
            wireMockServer.start();

            applicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
            applicationContext.addApplicationListener(
                    applicationEvent -> {
                        if (applicationEvent instanceof ContextClosedEvent) {
                            wireMockServer.stop();
                        }
                    });

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    String.format(
                            "rest-client.pull-payment.baseUrl=http://%s:%d/pullPaymentMock",
                            wireMockServer.getOptions().bindAddress(), wireMockServer.port()));
        }
    }

}