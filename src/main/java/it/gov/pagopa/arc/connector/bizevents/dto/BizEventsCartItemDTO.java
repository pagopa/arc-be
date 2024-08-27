package it.gov.pagopa.arc.connector.bizevents.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizEventsCartItemDTO {
    private String subject;
    private String amount;
    private BizEventsUserDetailDTO payee;
    private BizEventsUserDetailDTO debtor;
    private String refNumberValue;
    private String refNumberType;
}
