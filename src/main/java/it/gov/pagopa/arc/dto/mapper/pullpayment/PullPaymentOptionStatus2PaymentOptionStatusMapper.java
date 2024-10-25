package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.enums.PullPaymentOptionStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface PullPaymentOptionStatus2PaymentOptionStatusMapper {
    @ValueMapping(target = "UNPAID", source = "PO_UNPAID")
    @ValueMapping(target = "PAID", source = "PO_PAID")
    @ValueMapping(target = "PARTIALLY_REPORTED", source = "PO_PARTIALLY_REPORTED")
    @ValueMapping(target = "REPORTED", source = "PO_REPORTED")
    PaymentOptionStatus toPaymentOptionStatus(PullPaymentOptionStatus paymentOptionStatusSource);
}
