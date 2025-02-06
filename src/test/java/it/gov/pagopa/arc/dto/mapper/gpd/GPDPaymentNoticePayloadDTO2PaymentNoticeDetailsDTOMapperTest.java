package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentNoticeStatus;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticePayloadDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionDetailsDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapperTest {

    private GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper;

    @BeforeEach
    void setUp() {
        gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper = new GPDPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper();
    }

    @Test
    void givenGPDPaymentNoticePayloadDTOWhenMapThenReturnPaymentNoticeResponseDTO() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setNav("3".concat(gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).getIuv()));
        gpdPaymentNoticePayloadDTO.setStatus(GPDPaymentNoticeStatus.VALID);
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setNotificationFee(0L);
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then
        GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO = gpdPaymentNoticePayloadDTO.getPaymentOption().get(0);
        GPDTransferPayloadDTO gpdTransferPayloadDTO = gpdPaymentOptionPayloadDTO.getTransfer().get(0);

        Assertions.assertNotNull(result);
        assertEquals(gpdPaymentNoticePayloadDTO.getIupd(), result.getIupd());
        assertEquals(gpdTransferPayloadDTO.getOrganizationFiscalCode(),result.getPaTaxCode());
        assertEquals(gpdTransferPayloadDTO.getCompanyName(), result.getPaFullName());
        assertEquals(PaymentNoticeDetailsStatus.VALID, result.getStatus());

        PaymentOptionDetailsDTO paymentOptionDetailsDTO = result.getPaymentOptions().get(0);
        assertEquals(gpdPaymentOptionPayloadDTO.getNav(), paymentOptionDetailsDTO.getNav());
        assertEquals(gpdPaymentOptionPayloadDTO.getIuv(), paymentOptionDetailsDTO.getIuv());
        assertEquals(gpdPaymentOptionPayloadDTO.getAmount(), paymentOptionDetailsDTO.getAmount());
        assertEquals(gpdPaymentOptionPayloadDTO.getDescription(), paymentOptionDetailsDTO.getDescription());
        assertFalse(gpdPaymentOptionPayloadDTO.getIsPartialPayment());
        assertEquals(ZonedDateTime.of(gpdPaymentOptionPayloadDTO.getDueDate() , ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS), paymentOptionDetailsDTO.getDueDate());
        assertEquals(0L, paymentOptionDetailsDTO.getNotificationFee());

        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenNullGPDPaymentNoticePayloadDTOWhenMapThenReturnNull() {
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(null);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenNullGPDPaymentOptionPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.setPaymentOption(null);
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenEmptyGPDPaymentOptionPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.setPaymentOption(new ArrayList<>());
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenNullGPDTransferPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setTransfer(null);
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenEmptyGPDTransferPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setTransfer(new ArrayList<>());
        //when
        PaymentNoticeDetailsDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenGPDPaymentNoticeStatusWhenMapThenReturnPaymentNoticeDetailsStatus() {
        for (GPDPaymentNoticeStatus status : GPDPaymentNoticeStatus.values()) {
            PaymentNoticeDetailsStatus result = gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.gPDPaymentNoticeStatusToPaymentNoticeDetailsStatus(status);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(status.name(), result.name());
        }
        Assertions.assertNull(gpdPaymentNoticePayloadDTO2PaymentNoticeDetailsDTOMapper.gPDPaymentNoticeStatusToPaymentNoticeDetailsStatus(null));
    }

}