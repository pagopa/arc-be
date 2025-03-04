package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.fakers.connector.PaymentOptionDetailsDTOFaker;
import it.gov.pagopa.arc.fakers.connector.gpd.GPDPaymentNoticeDetailsDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDetailsStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionDetailsDTO;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapperTest {
    @Mock
    private GPDPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapper gpdPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapperMock;

    @InjectMocks
    private final GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper mapper = Mappers.getMapper(GPDPaymentNoticeDetailsDTO2PaymentNoticeDetailsDTOMapper.class);

    @Test
    void givenGPDPaymentNoticeDetailsDTOWhenToPaymentNoticeDetailsDTOThenReturnPaymentNoticeDetailsDTO() {
        //given
        List<PaymentOptionDetailsDTO> paymentOptionDetailsDTOList = PaymentOptionDetailsDTOFaker.mockInstance(1, false);
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTO = GPDPaymentNoticeDetailsDTOFaker.mockInstance(1, false);

        Mockito.when(gpdPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapperMock.toPaymentOptionDetailsDTO(gpdPaymentNoticeDetailsDTO.getPaymentOption().getFirst())).thenReturn(paymentOptionDetailsDTOList.getFirst());
        //when
        PaymentNoticeDetailsDTO result = mapper.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetailsDTO);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getIupd() , result.getIupd());
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getOrganizationFiscalCode() , result.getPaTaxCode());
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getCompanyName() , result.getPaFullName());
        Assertions.assertEquals(PaymentNoticeDetailsStatus.VALID , result.getStatus());
        Assertions.assertEquals(1, result.getPaymentOptions().size());
        Assertions.assertEquals(paymentOptionDetailsDTOList, result.getPaymentOptions());
        TestUtils.assertNotNullFields(result);
    }

    @Test
    void givenGPDPaymentNoticeDetailsDTOWithInstallmentsWhenToPaymentNoticeDetailsDTOThenReturnPaymentNoticeDetailsDTO() {
        //given
        List<PaymentOptionDetailsDTO> paymentOptionDetailsDTOList = PaymentOptionDetailsDTOFaker.mockInstance(2, true);
        GPDPaymentNoticeDetailsDTO gpdPaymentNoticeDetailsDTO = GPDPaymentNoticeDetailsDTOFaker.mockInstance(2, true);

        Mockito.when(gpdPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapperMock.toPaymentOptionDetailsDTO(gpdPaymentNoticeDetailsDTO.getPaymentOption().getFirst())).thenReturn(paymentOptionDetailsDTOList.getFirst());
        Mockito.when(gpdPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapperMock.toPaymentOptionDetailsDTO(gpdPaymentNoticeDetailsDTO.getPaymentOption().get(1))).thenReturn(paymentOptionDetailsDTOList.get(1));
        //when
        PaymentNoticeDetailsDTO result = mapper.toPaymentNoticeDetailsDTO(gpdPaymentNoticeDetailsDTO);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getIupd() , result.getIupd());
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getOrganizationFiscalCode() , result.getPaTaxCode());
        Assertions.assertEquals(gpdPaymentNoticeDetailsDTO.getCompanyName() , result.getPaFullName());
        Assertions.assertEquals(PaymentNoticeDetailsStatus.VALID , result.getStatus());
        Assertions.assertEquals(2, result.getPaymentOptions().size());
        Assertions.assertEquals(paymentOptionDetailsDTOList, result.getPaymentOptions());
        TestUtils.assertNotNullFields(result);
        Mockito.verify(gpdPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapperMock, Mockito.times(2)).toPaymentOptionDetailsDTO(any());
    }

}