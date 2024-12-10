package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.connector.gpd.GPDConnector;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.fakers.connector.PaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GPDServiceImplTest {
    private static final String USER_ID = "user_id";
    private static final String IUPD = "IUPD";
    private static final String ORGANIZATION_FISCAL_CODE = "DUMMY_ORGANIZATION_FISCAL_CODE";

    @Mock
    private GPDConnector gpdConnectorMock;
    @Mock
    private GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock;

    GPDService gpdService;

    @BeforeEach
    void setUp() {
        gpdService = new GPDServiceImpl(gpdConnectorMock, gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock);
    }

    @Test
    void givenGPDPaymentNoticeDetailsDTOWhenRetrievePaymentNoticeDetailsFromGPDThenReturnPaymentNoticeDetailsDTO() {
        //given
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTO = GPDPaymentNoticeDetailsDTOFaker.mockInstance(1, false);
        PaymentNoticeDetailsDTO paymentNoticeDetailsDTO = PaymentNoticeDetailsDTOFaker.mockInstance(1, false);

        Mockito.when(gpdConnectorMock.getPaymentNoticeDetails(USER_ID, ORGANIZATION_FISCAL_CODE, IUPD)).thenReturn(gpdPaymentNoticeDetailsDTO);
        Mockito.when(gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetailsDTO)).thenReturn(paymentNoticeDetailsDTO);
        //when
        PaymentNoticeDetailsDTO result = gpdService.retrievePaymentNoticeDetailsFromGPD(USER_ID, ORGANIZATION_FISCAL_CODE, IUPD);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(paymentNoticeDetailsDTO, result);
        Assertions.assertEquals(1, result.getPaymentOptions().size());
        Mockito.verifyNoMoreInteractions(gpdConnectorMock, gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock);
    }

    @Test
    void givenGPDPaymentNoticeDetailsDTOWithInstallmentsWhenRetrievePaymentNoticeDetailsFromGPDThenReturnPaymentNoticeDetailsDTO() {
        //given
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTO = GPDPaymentNoticeDetailsDTOFaker.mockInstance(2, true);
        PaymentNoticeDetailsDTO paymentNoticeDetailsDTO = PaymentNoticeDetailsDTOFaker.mockInstance(2, true);

        Mockito.when(gpdConnectorMock.getPaymentNoticeDetails(USER_ID, ORGANIZATION_FISCAL_CODE, IUPD)).thenReturn(gpdPaymentNoticeDetailsDTO);
        Mockito.when(gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetailsDTO)).thenReturn(paymentNoticeDetailsDTO);
        //when
        PaymentNoticeDetailsDTO result = gpdService.retrievePaymentNoticeDetailsFromGPD(USER_ID, ORGANIZATION_FISCAL_CODE, IUPD);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(paymentNoticeDetailsDTO, result);
        Assertions.assertEquals(2, result.getPaymentOptions().size());
        Mockito.verifyNoMoreInteractions(gpdConnectorMock, gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock);
        System.out.println(result.getPaymentOptions());
    }

}