package it.gov.pagopa.arc.dto.mapper.pullpayment;

import it.gov.pagopa.arc.connector.pullpayment.dto.PullPaymentOptionDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.PaymentOptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {PullPaymentInstallmentDTO2InstallmentDTOMapper.class, MapperUtilities.class})
public interface PullPaymentOptionDTO2PaymentOptionDTOMapper {
    @Mapping(source = "dueDate", target = "dueDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "convertToZonedDateTimeAndTruncateSeconds")
    PaymentOptionDTO toPaymentOptionDTO(PullPaymentOptionDTO pullPaymentOptionDTOSource);
}
