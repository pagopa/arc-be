package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PullPaymentOptionStatus2PaymentOptionStatus.class)
public interface PullPaymentInstallmentDTO2InstallmentDTO {
    InstallmentDTO toInstallmentDTO(PullPaymentInstallmentDTO pullPaymentInstallmentDTOSource);
}
