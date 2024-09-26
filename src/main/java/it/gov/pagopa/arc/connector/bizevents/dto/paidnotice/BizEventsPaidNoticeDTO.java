package it.gov.pagopa.arc.connector.bizevents.dto.paidnotice;

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
public class BizEventsPaidNoticeDTO {

    private String eventId;
    private String payeeName;
    private String payeeTaxCode;
    private String amount;
    private String noticeDate;
    private Boolean isCart;
    private Boolean isPayer;
    private Boolean isDebtor;
}
