package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentNoticeStatus;
import it.gov.pagopa.arc.model.generated.PaymentNoticeStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class PullPaymentNoticeStatus2PaymentNoticeStatusMapperTest {
    private final PullPaymentNoticeStatus2PaymentNoticeStatusMapper mapper = Mappers.getMapper(PullPaymentNoticeStatus2PaymentNoticeStatusMapper.class);

    @Test
    void givenPullPaymentNoticeStatusWhenCallMapperThenReturnPaymentNoticeStatus() {
        //given
        PullPaymentNoticeStatus valid = PullPaymentNoticeStatus.VALID;
        //when
        PaymentNoticeStatus result = mapper.toPaymentNoticeStatus(valid);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(PaymentNoticeStatus.VALID, result);
    }
}