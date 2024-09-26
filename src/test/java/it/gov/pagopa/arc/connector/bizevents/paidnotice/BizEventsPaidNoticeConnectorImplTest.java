package it.gov.pagopa.arc.connector.bizevents.paidnotice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.bizevents.dto.paidnotice.BizEventsPaidNoticeListDTO;
import it.gov.pagopa.arc.utils.MemoryAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static it.gov.pagopa.arc.config.WireMockConfig.WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = WireMockConfig.WireMockInitializer.class,
        classes = {
                BizEventsPaidNoticeConnectorImpl.class,
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

    @BeforeEach
    void setUp() {
        Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("it.gov.pagopa.arc.connector.bizevents.paidnotice.BizEventsPaidNoticeConnectorImpl");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void givenHeaderAndParameterWhenCallBizEventsPaidNoticeConnectorThenReturnPaidNoticeList() {
        //given
        //when
        BizEventsPaidNoticeListDTO paidNoticeList = bizEventsPaidNoticeConnector.getPaidNoticeList("DUMMY_FISCAL_CODE_OK", "CONTINUATION_TOKEN", 1,null,null,"TRANSACTION_DATE","DESC");
        //then
        assertEquals(1, paidNoticeList.getPaidNoticeList().size());
        assertEquals("1", paidNoticeList.getPaidNoticeList().get(0).getEventId());
        assertEquals("Comune di Milano", paidNoticeList.getPaidNoticeList().get(0).getPayeeName());
        assertEquals("MI_XXX", paidNoticeList.getPaidNoticeList().get(0).getPayeeTaxCode());
        assertEquals("180,00", paidNoticeList.getPaidNoticeList().get(0).getAmount());
        assertEquals("2024-03-27T13:07:25Z", paidNoticeList.getPaidNoticeList().get(0).getNoticeDate());
        assertFalse(paidNoticeList.getPaidNoticeList().get(0).getIsCart());
        assertTrue(paidNoticeList.getPaidNoticeList().get(0).getIsPayer());
        assertTrue(paidNoticeList.getPaidNoticeList().get(0).getIsDebtor());

    }
}