package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentInstallmentDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.InstallmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {PullPaymentOptionStatus2PaymentOptionStatusMapper.class, MapperUtilities.class})
public interface PullPaymentInstallmentDTO2InstallmentDTOMapper {

    @Mapping(source = "dueDate", target = "dueDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "convertToZonedDateTimeAndTruncateSeconds")
    InstallmentDTO toInstallmentDTO(PullPaymentInstallmentDTO pullPaymentInstallmentDTOSource);
}
