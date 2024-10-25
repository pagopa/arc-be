package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentNoticeStatus;
import it.gov.pagopa.arc.model.generated.PaymentNoticeStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PullPaymentNoticeStatus2PaymentNoticeStatusMapper {

    PaymentNoticeStatus toPaymentNoticeStatus(PullPaymentNoticeStatus paymentNoticeStatusSource);
}