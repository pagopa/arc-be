package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {PullPaymentOptionStatus2PaymentOptionStatus.class, MapperUtilities.class})
public interface PullPaymentInstallmentDTO2InstallmentDTO {

    @Mapping(source = "insertedDate", target = "insertedDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "truncateToSeconds")
    @Mapping(source = "lastUpdatedDate", target = "lastUpdatedDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "truncateToSeconds")
    InstallmentDTO toInstallmentDTO(PullPaymentInstallmentDTO pullPaymentInstallmentDTOSource);
}
