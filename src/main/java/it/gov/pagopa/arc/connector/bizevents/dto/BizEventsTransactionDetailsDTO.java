package it.gov.pagopa.arc.connector.bizevents.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizEventsTransactionDetailsDTO {
    @JsonProperty("infoTransaction")
    private BizEventsInfoTransactionDTO bizEventsInfoTransactionDTO;
    @JsonProperty("carts")
    private List<BizEventsCartItemDTO> bizEventsCartsDTO;
}
