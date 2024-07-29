package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PullPaymentNoticeDTO2PaymentNoticeDTOTest {

    @Mock
    PullPaymentNoticeStatus2PaymentNoticeStatus pullPaymentNoticeStatus2PaymentNoticeStatusMock;
    @Mock
    PullPaymentOptionDTO2PaymentOptionDTO pullPaymentOptionDTO2PaymentOptionDTOMock;

    @InjectMocks
    PullPaymentNoticeDTO2PaymentNoticeDTO mapper = Mappers.getMapper(PullPaymentNoticeDTO2PaymentNoticeDTO.class);

    @Test
    void givenPullPaymentNoticeDTOWhenCallMapperThenReturnPaymentNoticeDTO() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO = PullPaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO = PaymentNoticeDTOFaker.mockInstance(true);

        Mockito.when(pullPaymentNoticeStatus2PaymentNoticeStatusMock.toPaymentNoticeStatus(pullPaymentNoticeDTO.getStatus())).thenReturn(PaymentNoticeStatus.VALID);
        Mockito.when(pullPaymentOptionDTO2PaymentOptionDTOMock.toPaymentOptionDTO(pullPaymentNoticeDTO.getPaymentOptions().get(0))).thenReturn(paymentNoticeDTO.getPaymentOptions().get(0));
        //when
        PaymentNoticeDTO result = mapper.toPaymentNoticeDTO(pullPaymentNoticeDTO);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(paymentNoticeDTO, result);
        Mockito.verify(pullPaymentNoticeStatus2PaymentNoticeStatusMock).toPaymentNoticeStatus(any());
        Mockito.verify(pullPaymentOptionDTO2PaymentOptionDTOMock).toPaymentOptionDTO(any());
    }
}