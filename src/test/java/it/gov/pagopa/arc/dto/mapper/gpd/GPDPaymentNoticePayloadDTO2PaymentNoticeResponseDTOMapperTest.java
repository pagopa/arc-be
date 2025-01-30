package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticePayloadDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeResponseDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GPDPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapperTest {

    private GPDPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper;

    @BeforeEach
    void setUp() {
        gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper = new GPDPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper();
    }

    @Test
    void givenGPDPaymentNoticePayloadDTOWhenMapThenReturnPaymentNoticeResponseDTO() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setNav("3".concat(gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).getIuv()));
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then
        GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO = gpdPaymentNoticePayloadDTO.getPaymentOption().get(0);
        GPDTransferPayloadDTO gpdTransferPayloadDTO = gpdPaymentOptionPayloadDTO.getTransfer().get(0);

        Assertions.assertNotNull(result);
        assertEquals("3".concat(gpdPaymentOptionPayloadDTO.getIuv()),result.getNav());
        assertEquals(gpdPaymentOptionPayloadDTO.getAmount() ,result.getAmount());
        assertEquals(gpdTransferPayloadDTO.getOrganizationFiscalCode(),result.getPaTaxCode());
        assertEquals(gpdTransferPayloadDTO.getCompanyName(), result.getPaFullName());
        assertEquals(gpdPaymentOptionPayloadDTO.getDescription(), result.getDescription());

        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenNullGPDPaymentNoticePayloadDTOWhenMapThenReturnNull() {
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(null);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenNullGPDPaymentOptionPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.setPaymentOption(null);
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenEmptyGPDPaymentOptionPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.setPaymentOption(new ArrayList<>());
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenNullGPDTransferPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setTransfer(null);
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }

    @Test
    void givenEmptyGPDTransferPayloadDTOWhenMapThenReturnNull() {
        //given
        GPDPaymentNoticePayloadDTO gpdPaymentNoticePayloadDTO = GPDPaymentNoticePayloadDTOFaker.mockInstance("DUMMY_ORGANIZATION_FISCAL_CODE");
        gpdPaymentNoticePayloadDTO.getPaymentOption().get(0).setTransfer(new ArrayList<>());
        //when
        PaymentNoticeResponseDTO result = gpdPaymentNoticePayloadDTO2PaymentNoticeResponseDTOMapper.map(gpdPaymentNoticePayloadDTO);
        //then

        Assertions.assertNull(result);
    }
}