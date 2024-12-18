package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.enums.GPDPaymentOptionDetailsStatus;
import it.gov.pagopa.arc.model.generated.PaymentOptionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface GPDPaymentOptionDetailsStatus2PaymentOptionStatusMapper {

    @ValueMapping(target = "UNPAID", source = "PO_UNPAID")
    @ValueMapping(target = "PAID", source = "PO_PAID")
    @ValueMapping(target = "PARTIALLY_REPORTED", source = "PO_PARTIALLY_REPORTED")
    @ValueMapping(target = "REPORTED", source = "PO_REPORTED")
    PaymentOptionStatus toPaymentOptionStatus(GPDPaymentOptionDetailsStatus gpdPaymentOptionDetailsStatus);
}
