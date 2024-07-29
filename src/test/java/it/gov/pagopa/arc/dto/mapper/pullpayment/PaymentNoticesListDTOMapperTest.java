package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.fakers.paymentNotices.PaymentNoticeDTOFaker;
import it.gov.pagopa.arc.model.generated.PaymentNoticeDTO;
import it.gov.pagopa.arc.model.generated.PaymentNoticesListDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

class PaymentNoticesListDTOMapperTest {
    private final PaymentNoticesListDTOMapper mapper = Mappers.getMapper(PaymentNoticesListDTOMapper.class);

    @Test
    void givenListOfPullPaymentNoticesListDTOWhenCallMapperThenReturnPaymentNoticesListDTO() {
        //given
        PaymentNoticeDTO paymentNoticeDTO1 = PaymentNoticeDTOFaker.mockInstance(true);
        PaymentNoticeDTO paymentNoticeDTO2 = PaymentNoticeDTOFaker.mockInstance(true);

        List<PaymentNoticeDTO> listOfPaymentNoticeDto = List.of(paymentNoticeDTO1, paymentNoticeDTO2);
        //when

        PaymentNoticesListDTO result = mapper.toPaymentNoticesListDTO(listOfPaymentNoticeDto);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getPaymentNotices().size());
        Assertions.assertEquals(paymentNoticeDTO1, result.getPaymentNotices().get(0));
        Assertions.assertEquals(paymentNoticeDTO2, result.getPaymentNotices().get(1));
    }

    @Test
    void givenEmptyListWhenCallMapperThenReturnPaymentNoticesListDTOWithEmptyList() {
        //given
        List<PaymentNoticeDTO> listOfPaymentNoticeDto = new ArrayList<>();

        //when
        PaymentNoticesListDTO result = mapper.toPaymentNoticesListDTO(listOfPaymentNoticeDto);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getPaymentNotices().isEmpty());

    }
}