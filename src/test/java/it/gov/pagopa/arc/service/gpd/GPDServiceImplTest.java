package it.gov.pagopa.arc.service.gpd;

import it.gov.pagopa.arc.connector.gpd.GPDConnector;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.gpd.GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper;
import it.gov.pagopa.arc.dto.mapper.gpd.PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper;
import it.gov.pagopa.arc.fakers.PaymentNoticePayloadDTOFaker;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.fakers.connector.PaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticePayloadDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;
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
    @Mock
    private PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapperMock;
    @Mock
    private GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapperMock;


    GPDService gpdService;

    @BeforeEach
    void setUp() {
        gpdService = new GPDServiceImpl(gpdConnectorMock, gpdPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperMock, paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapperMock, gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapperMock);
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
    }

    @Test
    void givenPaymentNoticePayloadDTOWhenGeneratePaymentNoticeFromGPDThenReturnPaymentNoticeResponseDTO() {
        //given
        IamUserInfoDTO iamUserInfoDTO = IamUserInfoDTOFaker.mockInstance();
        PaymentNoticePayloadDTO paymentNoticePayloadDTO = PaymentNoticePayloadDTOFaker.mockInstance();

        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("ORGANIZATION_FISCAL_CODE");

        GPDPaymentNoticePayloadDTO gpdPaymentNoticeResponse = GPDPaymentNoticePayloadDTOFaker.mockInstance("ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticeResponse.getPaymentOption().getFirst().setNav("302040501822520951");

        PaymentNoticeDetailsDTO expected = PaymentNoticeDetailsDTOFaker.mockInstance(1, false);

        Mockito.when(gpdConnectorMock.generatePaymentNotice("ORGANIZATION_FISCAL_CODE", gpdPaymentNoticePayloadDTO)).thenReturn(gpdPaymentNoticeResponse);
        Mockito.when(paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapperMock.map(iamUserInfoDTO, paymentNoticePayloadDTO)).thenReturn(gpdPaymentNoticePayloadDTO);
        Mockito.when(gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapperMock.map(gpdPaymentNoticeResponse)).thenReturn(expected);
        //when
        PaymentNoticeDetailsDTO result = gpdService.generatePaymentNoticeFromGPD(iamUserInfoDTO, paymentNoticePayloadDTO);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
        Mockito.verifyNoMoreInteractions(gpdConnectorMock, paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapperMock, gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapperMock);
    }
}