package it.gov.pagopa.arc.connector.bizevents;

import ch.qos.logback.classic.LoggerContext;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionDetailsDTO;
import it.gov.pagopa.arc.connector.bizevents.dto.BizEventsTransactionsListDTO;
import it.gov.pagopa.arc.connector.bizevents.enums.Origin;
import it.gov.pagopa.arc.connector.bizevents.enums.PaymentMethod;
import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import it.gov.pagopa.arc.exception.custom.BizEventsReceiptNotFoundException;
import it.gov.pagopa.arc.exception.custom.BizEventsTransactionNotFoundException;
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
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                "rest-client.biz-events.api-key=x_api_key0"
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
                ()-> bizEventsConnector.getTransactionsList("DUMMY_FISCAL_CODE_ERROR", 2));

    }

    @Test
    void givenTransactionIdWhenCallBizEventsConnectorThenReturnTransactionDetails() {
        //given
        //when
        BizEventsTransactionDetailsDTO transactionDetails = bizEventsConnector.getTransactionDetails("DUMMY_FISCAL_CODE_DETAILS", "TRANSACTION_ID_OK_1");

        //then
        Assertions.assertNotNull(transactionDetails.getBizEventsInfoTransactionDTO());
        Assertions.assertEquals(1, transactionDetails.getBizEventsCartsDTO().size());
        Assertions.assertEquals( "TRANSACTION_ID", transactionDetails.getBizEventsInfoTransactionDTO().getTransactionId());
        Assertions.assertEquals( "250863", transactionDetails.getBizEventsInfoTransactionDTO().getAuthCode());
        Assertions.assertEquals( "223560110624", transactionDetails.getBizEventsInfoTransactionDTO().getRrn());
        Assertions.assertEquals( "2024-06-13T15:22:04Z", transactionDetails.getBizEventsInfoTransactionDTO().getTransactionDate());
        Assertions.assertEquals( "Worldline Merchant Services Italia S.p.A.", transactionDetails.getBizEventsInfoTransactionDTO().getPspName());

        Assertions.assertEquals( "ERNESTO HOLDER", transactionDetails.getBizEventsInfoTransactionDTO().getBizEventsWalletInfoDTO().getAccountHolder());
        Assertions.assertEquals( "MASTERCARD", transactionDetails.getBizEventsInfoTransactionDTO().getBizEventsWalletInfoDTO().getBrand());
        Assertions.assertEquals( "0403", transactionDetails.getBizEventsInfoTransactionDTO().getBizEventsWalletInfoDTO().getBlurredNumber());

        Assertions.assertEquals( PaymentMethod.PO, transactionDetails.getBizEventsInfoTransactionDTO().getPaymentMethod());
        Assertions.assertEquals( "ERNESTO PAYER", transactionDetails.getBizEventsInfoTransactionDTO().getPayer().getName());
        Assertions.assertEquals( "TAX_CODE", transactionDetails.getBizEventsInfoTransactionDTO().getPayer().getTaxCode());

        Assertions.assertEquals("634,37", transactionDetails.getBizEventsInfoTransactionDTO().getAmount());
        Assertions.assertEquals("0,53", transactionDetails.getBizEventsInfoTransactionDTO().getFee());
        Assertions.assertEquals(Origin.UNKNOWN, transactionDetails.getBizEventsInfoTransactionDTO().getOrigin());

        Assertions.assertEquals("pagamento", transactionDetails.getBizEventsCartsDTO().get(0).getSubject());
        Assertions.assertEquals("634,37", transactionDetails.getBizEventsCartsDTO().get(0).getAmount());

        Assertions.assertEquals("ACI Automobile Club Italia", transactionDetails.getBizEventsCartsDTO().get(0).getPayee().getName());
        Assertions.assertEquals("00493410583", transactionDetails.getBizEventsCartsDTO().get(0).getPayee().getTaxCode());

        Assertions.assertEquals("ERNESTO PAYER", transactionDetails.getBizEventsCartsDTO().get(0).getDebtor().getName());
        Assertions.assertEquals("TAX_CODE", transactionDetails.getBizEventsCartsDTO().get(0).getDebtor().getTaxCode());

        Assertions.assertEquals("960000000094659945", transactionDetails.getBizEventsCartsDTO().get(0).getRefNumberValue());
        Assertions.assertEquals("IUV", transactionDetails.getBizEventsCartsDTO().get(0).getRefNumberType());
    }

    @Test
    void givenTransactionIdWhenNotFoundThenReturnException() {
        //given
        //when
        BizEventsTransactionNotFoundException bizEventsTransactionNotFoundException = assertThrows(BizEventsTransactionNotFoundException.class,
                () -> bizEventsConnector.getTransactionDetails("DUMMY_FISCAL_CODE_NOT_FOUND", "TRANSACTION_ID_NOT_FOUND_1"));
        Assertions.assertEquals( "An error occurred handling request from biz-Events to retrieve transaction with transaction id [TRANSACTION_ID_NOT_FOUND_1] for the current user", bizEventsTransactionNotFoundException.getMessage());
    }

    @Test
    void givenTransactionIdWhenErrorThenThrowBizEventsInvocationException() {
        //When
        //Then
        BizEventsInvocationException bizEventsInvocationException = assertThrows(BizEventsInvocationException.class,
                () -> bizEventsConnector.getTransactionDetails("DUMMY_FISCAL_CODE_ERROR", "TRANSACTION_ID_ERROR_1"));
        Assertions.assertEquals( "An error occurred handling request from biz-Events", bizEventsInvocationException.getMessage());
    }

    @Test
    void givenTransactionIdWhenCallBizEventsConnectorThenReturnTransactionReceipt() throws IOException {
        //given
        //when
        Resource transactionReceipt = bizEventsConnector.getTransactionReceipt("DUMMY_FISCAL_CODE_RECEIPT", "TRANSACTION_ID_RECEIPT_OK_1");

        //then
        Assertions.assertNotNull(transactionReceipt);
        Assertions.assertTrue(transactionReceipt.exists());
        byte[] expectedContent = Files.readAllBytes(Paths.get("src/test/resources/stub/__files/testReceiptPdfFile.pdf"));
        byte[] actualContent = transactionReceipt.getInputStream().readAllBytes();
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    void givenTransactionIdWhenReceiptNotFoundThenReturnException() {
        //given
        //when
        BizEventsReceiptNotFoundException bizEventsReceiptNotFoundException = assertThrows(BizEventsReceiptNotFoundException.class,
                () -> bizEventsConnector.getTransactionReceipt("DUMMY_FISCAL_CODE_RECEIPT_NOT_FOUND", "TRANSACTION_ID_RECEIPT_NOT_FOUND_1"));
        Assertions.assertEquals( "An error occurred handling request from biz-Events to retrieve transaction receipt with transaction id [TRANSACTION_ID_RECEIPT_NOT_FOUND_1] for the current user", bizEventsReceiptNotFoundException.getMessage());
    }

    @Test
    void givenTransactionIdWhenReceiptErrorThenThrowBizEventsInvocationException() {
        //When
        //Then
        BizEventsInvocationException bizEventsInvocationException = assertThrows(BizEventsInvocationException.class,
                () -> bizEventsConnector.getTransactionReceipt("DUMMY_FISCAL_CODE_RECEIPT_ERROR", "TRANSACTION_ID_RECEIPT_ERROR_1"));
        Assertions.assertEquals( "An error occurred handling request from biz-Events", bizEventsInvocationException.getMessage());
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
                            "rest-client.biz-events.baseUrl=http://%s:%d/bizEventsMock",
                            wireMockServer.getOptions().bindAddress(), wireMockServer.port()));
        }
    }

}
