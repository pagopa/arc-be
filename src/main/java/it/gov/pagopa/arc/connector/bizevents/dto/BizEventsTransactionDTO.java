package it.gov.pagopa.arc.connector.bizevents.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizEventsTransactionDTO {
    private String transactionId;
    private String payeeName;
    private String payeeTaxCode;
    private String amount;
    private String transactionDate;
    private Boolean isCart;
    private Boolean isPayer;
    private Boolean isDebtor;
}