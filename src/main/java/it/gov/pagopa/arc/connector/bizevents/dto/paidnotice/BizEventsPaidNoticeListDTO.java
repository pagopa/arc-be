package it.gov.pagopa.arc.connector.bizevents.dto.paidnotice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizEventsPaidNoticeListDTO {
    private List<BizEventsPaidNoticeDTO> notices;
}
