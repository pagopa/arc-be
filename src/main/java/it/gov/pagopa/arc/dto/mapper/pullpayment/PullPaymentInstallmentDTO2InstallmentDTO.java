package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.dto.mapper.DateTimeTruncate;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PullPaymentOptionStatus2PaymentOptionStatus.class, DateTimeTruncate.class})
public interface PullPaymentInstallmentDTO2InstallmentDTO {

    @Mapping(source = "insertedDate", target = "insertedDate",qualifiedByName = "truncateToSeconds")
    @Mapping(source = "lastUpdatedDate", target = "lastUpdatedDate",qualifiedByName = "truncateToSeconds")
    InstallmentDTO toInstallmentDTO(PullPaymentInstallmentDTO pullPaymentInstallmentDTOSource);
}
