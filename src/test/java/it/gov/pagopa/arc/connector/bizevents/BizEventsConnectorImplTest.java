package it.gov.pagopa.arc.connector.bizevents;

import ch.qos.logback.classic.LoggerContext;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
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
import utils.MemoryAppender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = BizEventsConnectorImplTest.WireMockInitializer.class,
        classes = {
                BizEventsConnectorImpl.class,
                FeignConfig.class,
                BizEventsRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(
        properties = {
                "rest-client.biz-events.api-key=x_api_key0",
})
class BizEventsConnectorImplTest {

    @Autowired
     private BizEventsConnector bizEventsConnector;
    private MemoryAppender memoryAppender;

    @BeforeEach
    void setUp() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.connector.bizevents.BizEventsConnectorImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenHeaderAndParameterWhenCallBizEventsConnectorThenReturnTransactionList() {
        //given
        //when
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE", "TOKEN", 1);

        //then
        assertEquals(1, bizEventsTransactionsListDTO.getTransactions().size());
        assertEquals("1", bizEventsTransactionsListDTO.getTransactions().get(0).getTransactionId());
        assertEquals("Comune di Milano", bizEventsTransactionsListDTO.getTransactions().get(0).getPayeeName());
        assertEquals("MI_XXX", bizEventsTransactionsListDTO.getTransactions().get(0).getPayeeTaxCode());
        assertEquals("180,00", bizEventsTransactionsListDTO.getTransactions().get(0).getAmount());
        assertEquals("2024-03-27T13:07:25Z", bizEventsTransactionsListDTO.getTransactions().get(0).getTransactionDate());
        assertFalse(bizEventsTransactionsListDTO.getTransactions().get(0).getIsCart());
        assertTrue(bizEventsTransactionsListDTO.getTransactions().get(0).getIsPayer());
        assertTrue(bizEventsTransactionsListDTO.getTransactions().get(0).getIsDebtor());

    }

    @Test
    void givenHeaderAndParameterWhenNotFoundThenReturnEmptyTransactionList() {
        //given
        //when
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE_NOT_FOUND", "TOKEN", 2);
        //then
        Assertions.assertEquals(0,bizEventsTransactionsListDTO.getTransactions().size());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred handling request getTransactionsList from biz-Events: HttpStatus 404"))
        );
    }
    @Test
    void givenHeaderAndParameterWhenErrorThenThrowBizEventsInvocationException() {
        //When
        //Then
        Assertions.assertThrows(BizEventsInvocationException.class,
                ()-> bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE_ERROR", "TOKEN", 2));

    }

    public static class WireMockInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().usingFilesUnderClasspath("src/test/resources/stub"));
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
                            "rest-client.biz-events.baseUrl=http://%s:%d/bizEventsMock",
                            wireMockServer.getOptions().bindAddress(), wireMockServer.port()));
        }
    }
}
