package it.gov.pagopa.arc.dto;

import it.gov.pagopa.arc.model.generated.NoticesListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticesListResponseDTO {
    private NoticesListDTO noticesListDTO;
    private String continuationToken;
}
