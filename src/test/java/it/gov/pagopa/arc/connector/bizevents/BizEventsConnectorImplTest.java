package it.gov.pagopa.arc.connector.bizevents;

import static it.gov.pagopa.arc.config.WireMockConfig.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = WireMockConfig.WireMockInitializer.class,
        classes = {
                BizEventsConnectorImpl.class,
                FeignConfig.class,
                BizEventsRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(
        properties = {
                "rest-client.biz-events.transactions.api-key=x_api_key0",
                WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "rest-client.biz-events.transactions.baseUrl=bizEventsMock"
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
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE", 1);
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
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE_NOT_FOUND", 2);
        //then
        Assertions.assertEquals(0, bizEventsTransactionsListDTO.getTransactions().size());
        Assertions.assertTrue(memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
                .contains(("A class feign.FeignException$NotFound occurred handling request getTransactionsList from biz-Events: HttpStatus 404"))
        );
    }

    @Test
    void givenHeaderAndParameterWhenErrorThenThrowBizEventsInvocationException() {
        //When
        //Then
        Assertions.assertThrows(BizEventsInvocationException.class,
                () -> bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE_ERROR", 2));

    }

}
