package it.gov.pagopa.arc.connector.bizevents;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
                "apiKey.biz-events=x_api_key0",
})
class BizEventsConnectorImplTest {

    @Autowired
     private BizEventsConnector bizEventsConnector;

    @Test
    void givenHeaderAndParameterWhenCallBizEventsConnectorThenReturnTransactionList() {
        //given
        //when
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE", "TOKEN", 1);

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
        //then
        BizEventsTransactionsListDTO bizEventsTransactionsListDTO = bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE_404", "TOKEN", 2);
        Assertions.assertEquals(0,bizEventsTransactionsListDTO.getTransactions().size());
    }

    @Test
    void givenHeaderAndParameterWhenUnauthorizedThenThrowRuntimeException() {
        //given
        //when
        //then
        Assertions.assertThrows(RuntimeException.class,
                ()-> bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE_401", "TOKEN", 2));
    }

    @Test
    void givenHeaderAndParameterWhenTooManyRequestThenThrowRuntimeException() {
        //given
        //when
        //then
        Assertions.assertThrows(RuntimeException.class,
                ()-> bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE_429", "TOKEN", 2));
    }

    @Test
    void givenHeaderAndParameterWhenInternalServerErrorThenThrowRuntimeException() {
        //given
        //when
        //then
        Assertions.assertThrows(RuntimeException.class,
                ()-> bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE_500", "TOKEN", 2));
    }

    @Test
    void givenHeaderAndParameterWhenDefaultThenThrowRuntimeException() {
        //given
        //when
        //then
        Assertions.assertThrows(RuntimeException.class,
                ()-> bizEventsConnector.transactionsList("DUMMY_FISCAL_CODE_DEFAULT", "TOKEN", 2));
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
