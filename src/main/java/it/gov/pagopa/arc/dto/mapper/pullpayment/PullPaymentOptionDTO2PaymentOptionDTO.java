package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.model.generated.PaymentOptionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PullPaymentInstallmentDTO2InstallmentDTO.class)
public interface PullPaymentOptionDTO2PaymentOptionDTO {
    PaymentOptionDTO toPaymentOptionDTO(PullPaymentOptionDTO pullPaymentOptionDTOSource);
}
