package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.dto.NoticesListResponseDTO;
import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BizEventsPaidResponseDTO2NoticesListResponseDTO {
    default NoticesListResponseDTO toNoticesListResponseDTO(NoticesListDTO noticesListDTO, String continuationToken){
        return NoticesListResponseDTO.builder()
                .noticesListDTO(noticesListDTO)
                .continuationToken(continuationToken)
                .build();
    }
}
