package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticePayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.dto.GPDTransferPayloadDTO;
import it.gov.pagopa.arc.connector.gpd.enums.GPDDebtorType;
import it.gov.pagopa.arc.dto.IamUserInfoDTO;
import it.gov.pagopa.arc.fakers.PaymentNoticePayloadDTOFaker;
import it.gov.pagopa.arc.fakers.auth.IamUserInfoDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticePayloadDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapperTest {

    private PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper;
    private IamUserInfoDTO iamUserInfoDTO;

    @BeforeEach
    void setUp() {
        paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper = new PaymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper();
        iamUserInfoDTO = IamUserInfoDTOFaker.mockInstance();
    }

    @Test
    void givenPaymentNoticePayloadDTOWhenMapThenReturnGPDPaymentNoticePayloadDTO() {
        //given

        PaymentNoticePayloadDTO paymentNoticePayloadDTO = PaymentNoticePayloadDTOFaker.mockInstance();
        //when
        GPDPaymentNoticePayloadDTO result = paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper.map(iamUserInfoDTO, paymentNoticePayloadDTO);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getIupd().contains("ARp"));
        Assertions.assertEquals(40, result.getIupd().length());
        Assertions.assertEquals(GPDDebtorType.F, result.getType());
        Assertions.assertEquals("FISCAL-CODE789456", result.getFiscalCode());
        Assertions.assertEquals("name familyName", result.getFullName());
        Assertions.assertTrue(result.getSwitchToExpired());
        Assertions.assertEquals("ORGANIZATION_NAME", result.getCompanyName());

        Assertions.assertEquals(1, result.getPaymentOption().size());
        GPDPaymentOptionPayloadDTO gpdPaymentOptionPayloadDTO = result.getPaymentOption().getFirst();
        Assertions.assertTrue(gpdPaymentOptionPayloadDTO.getIuv().startsWith("02"));
        Assertions.assertEquals(17, gpdPaymentOptionPayloadDTO.getIuv().length());
        Assertions.assertEquals(120L, gpdPaymentOptionPayloadDTO.getAmount());
        Assertions.assertEquals("Test description", gpdPaymentOptionPayloadDTO.getDescription());
        Assertions.assertFalse(gpdPaymentOptionPayloadDTO.getIsPartialPayment());
        Assertions.assertTrue(gpdPaymentOptionPayloadDTO.getDueDate().isAfter(LocalDateTime.now().plusDays(4)));

        GPDTransferPayloadDTO gpdTransferPayloadDTO = gpdPaymentOptionPayloadDTO.getTransfer().getFirst();
        Assertions.assertNotNull(gpdPaymentOptionPayloadDTO.getTransfer());
        Assertions.assertEquals(1, gpdPaymentOptionPayloadDTO.getTransfer().size());
        Assertions.assertEquals("1", gpdTransferPayloadDTO.getIdTransfer());
        Assertions.assertEquals(120L, gpdTransferPayloadDTO.getAmount());
        Assertions.assertEquals("Test description", gpdTransferPayloadDTO.getRemittanceInformation());
        Assertions.assertEquals("9/0101108TS/", gpdTransferPayloadDTO.getCategory());
        Assertions.assertEquals("IT39X0300203280451585346538", gpdTransferPayloadDTO.getIban());

        TestUtils.assertNotNullFields(result,
                "streetName",
                "civicNumber",
                "postalCode",
                "city",
                "province",
                "region",
                "country",
                "email",
                "phone",
                "officeName",
                "validityDate",
                "paymentDate",
                "status");

        TestUtils.assertNotNullFields(gpdPaymentOptionPayloadDTO,
                "nav",
                "fee",
                "notificationFee",
                "retentionDate",
                "paymentOptionMetadata");
    }

    @Test
    void givenNullWhenMapThenReturnNull(){
        //when
        GPDPaymentNoticePayloadDTO result = paymentNoticePayloadDTO2GPDPaymentNoticePayloadDTOMapper.map(null,null);

        //then
        Assertions.assertNull(result);
    }
}