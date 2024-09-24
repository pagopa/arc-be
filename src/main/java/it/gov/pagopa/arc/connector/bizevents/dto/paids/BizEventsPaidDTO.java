package it.gov.pagopa.arc.connector.bizevents.dto.paids;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizEventsPaidDTO {

    private String eventId;
    private String payeeName;
    private String payeeTaxCode;
    private Long amount;
    private ZonedDateTime noticeDate;
    private Boolean isCart;
    private Boolean isPayer;
    private Boolean isDebtor;
}
