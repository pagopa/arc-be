package it.gov.pagopa.arc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    private String transactionId;
    private String payeeTaxCode;
    private String amount;
    private ZonedDateTime transactionDate;
    private Boolean isCart;
    private Boolean payedByMe;
    private Boolean registeredToMe;
}
