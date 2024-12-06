package it.gov.pagopa.arc.connector.gpd;

import it.gov.pagopa.arc.config.FeignConfig;
import it.gov.pagopa.arc.config.WireMockConfig;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticeDetailsDTOFaker;
import org.junit.jupiter.api.Test;
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
                GPDConnectorImpl.class,
                FeignConfig.class,
                GPDRestClient.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
@TestPropertySource(properties = {
        WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX + "rest-client.gpd.baseUrl=gpdMock",
        "rest-client.gpd.api-key=x_api_key0"
})
class GPDConnectorImplTest {

    @Autowired
    private GPDConnector gpdConnector;

    @Test
    void givenPDWithSinglePOWhenGetPaymentNoticeDetailsThenReturnGPDPaymentNoticeDetails() {
        //given
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTOExpected = GPDPaymentNoticeDetailsDTOFaker.mockInstance(1, false);

        //when
        GPDPaymentNoticeDetailsDTO result = gpdConnector.getPaymentNoticeDetails("USER_ID", "DUMMY_ORGANIZATION_FISCAL_CODE", "IUPD_OK_0");

        //then
        System.out.println("EXPECTED" + gpdPaymentNoticeDetailsDTOExpected);
        System.out.println("RESULT" + result);
        assertNotNull(result);
        assertEquals(gpdPaymentNoticeDetailsDTOExpected, result);
        assertEquals(1, result.getPaymentOption().size());
        assertFalse(result.getPaymentOption().get(0).getIsPartialPayment());

    }

    @Test
    void givenPDWithInstallmentsPOWhenGetPaymentNoticeDetailsThenReturnGPDPaymentNoticeDetails() {
        //given
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTOExpected = GPDPaymentNoticeDetailsDTOFaker.mockInstance(2, true);

        //when
        GPDPaymentNoticeDetailsDTO result = gpdConnector.getPaymentNoticeDetails("USER_ID", "DUMMY_ORGANIZATION_FISCAL_CODE", "IUPD_INSTALLMENTS_OK_2");
        System.out.println(gpdPaymentNoticeDetailsDTOExpected);
        //then
        assertNotNull(result);
        assertEquals(gpdPaymentNoticeDetailsDTOExpected, result);
        assertEquals(2, result.getPaymentOption().size());
        assertTrue(result.getPaymentOption().get(0).getIsPartialPayment());
        assertTrue(result.getPaymentOption().get(1).getIsPartialPayment());
    }

    @Test
    void givenNotFoundIUPDWhenGetPaymentNoticeDetailsThenThrowException() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gpdConnector.getPaymentNoticeDetails("USER_ID", "DUMMY_ORGANIZATION_FISCAL_CODE", "IUPD_NOT_FOUND_0"));
        assertEquals("An error occurred handling request from GPD to retrieve payment notice with organizationFiscalCode [DUMMY_ORGANIZATION_FISCAL_CODE] and iupd [IUPD_NOT_FOUND_0] for the current user with userId [USER_ID]", ex.getMessage());
    }

    @Test
    void givenWrongIUPDWhenGetPaymentNoticeDetailsThenThrowException() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gpdConnector.getPaymentNoticeDetails("USER_ID", "DUMMY_ORGANIZATION_FISCAL_CODE", "IUPD_BAD_REQUEST_0"));
        assertEquals("One or more inputs provided during the request from GPD are invalid", ex.getMessage());
    }

    @Test
    void givenErrorIUPDWhenGetPaymentNoticeDetailsThenThrowException() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gpdConnector.getPaymentNoticeDetails("USER_ID", "DUMMY_ORGANIZATION_FISCAL_CODE", "IUPD_ERROR_0"));
        assertEquals("An error occurred handling request from GPD service", ex.getMessage());
    }
}