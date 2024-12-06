package it.gov.pagopa.arc.dto.mapper.gpd;

import it.gov.pagopa.arc.connector.gpd.dto.GPDPaymentOptionDetailsDTO;
import it.gov.pagopa.arc.dto.mapper.MapperUtilities;
import it.gov.pagopa.arc.model.generated.PaymentOptionDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {MapperUtilities.class, GPDPaymentOptionDetailsStatus2PaymentOptionStatusMapper.class})
public interface GPDPaymentOptionDetailsDTO2PaymentOptionDetailsDTOMapper {

    @Mapping(source = "dueDate", target = "dueDate", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "convertToZonedDateTimeAndTruncateSeconds")
    PaymentOptionDetailsDTO toPaymentOptionDetailsDTO(GPDPaymentOptionDetailsDTO gpdPaymentOptionDetailsDTO);
}
