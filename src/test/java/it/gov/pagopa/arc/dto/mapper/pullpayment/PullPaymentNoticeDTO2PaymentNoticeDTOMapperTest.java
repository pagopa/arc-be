package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentNoticeDTO;
import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.fakers.connector.pullPayment.PullPaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticeStatus;
import it.gov.pagopa.arc.utils.TestUtils;
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
class PullPaymentNoticeDTO2PaymentNoticeDTOMapperTest {

    @Mock
    PullPaymentNoticeStatus2PaymentNoticeStatusMapper pullPaymentNoticeStatus2PaymentNoticeStatusMapperMock;
    @Mock
    PullPaymentOptionDTO2PaymentOptionDTOMapper pullPaymentOptionDTO2PaymentOptionDTOMapperMock;

    @InjectMocks
    PullPaymentNoticeDTO2PaymentNoticeDTOMapper mapper = Mappers.getMapper(PullPaymentNoticeDTO2PaymentNoticeDTOMapper.class);

    @Test
    void givenPullPaymentNoticeDTOWhenCallMapperThenReturnPaymentNoticeDTO() {
        //given
        PullPaymentNoticeDTO pullPaymentNoticeDTO = PullPaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO = PaymentNoticeDTOFaker.mockInstance(true);

        Mockito.when(pullPaymentNoticeStatus2PaymentNoticeStatusMapperMock.toPaymentNoticeStatus(pullPaymentNoticeDTO.getStatus())).thenReturn(PaymentNoticeStatus.VALID);
        Mockito.when(pullPaymentOptionDTO2PaymentOptionDTOMapperMock.toPaymentOptionDTO(pullPaymentNoticeDTO.getPaymentOptions().getFirst())).thenReturn(paymentNoticeDTO.getPaymentOptions().getFirst());
        //when
        PaymentNoticeDTO result = mapper.toPaymentNoticeDTO(pullPaymentNoticeDTO);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(paymentNoticeDTO, result);
        Mockito.verify(pullPaymentNoticeStatus2PaymentNoticeStatusMapperMock).toPaymentNoticeStatus(any());
        Mockito.verify(pullPaymentOptionDTO2PaymentOptionDTOMapperMock).toPaymentOptionDTO(any());

        TestUtils.assertNotNullFields(result);
    }
}